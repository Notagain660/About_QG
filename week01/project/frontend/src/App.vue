<template>
  <div style="padding: 20px; max-width: 400px; margin: auto;">
    <h2 class="title">🏠 宿舍报修系统</h2>

    <!-- 未登录状态 -->
    <div v-if="!loggedIn" class="auth-card">
      <div class="tab-bar">
        <div style="display: flex; justify-content: center; gap: 10px; margin-bottom: 20px;">
          <button :class="{ active: activeTab === 'login' }" @click="activeTab = 'login'">登录</button>
          <button :class="{ active: activeTab === 'register' }" @click="activeTab = 'register'">注册</button>
        </div>
      </div>

      <!-- 登录表单 -->
      <div v-if="activeTab === 'login'" class="form">
        <input v-model="loginId" placeholder="学号/工号" />
        <input v-model="loginPassword" type="password" placeholder="密码" />
        <button @click="doLogin">登录</button>
        <p class="msg">{{ message }}</p>
      </div>

      <!-- 注册表单 -->
      <div v-if="activeTab === 'register'" class="form">
        <select v-model="registerRole">
          <option value="STUDENT">学生</option>
          <option value="ADMIN">管理员</option>
        </select>
        <input v-model="registerId" placeholder="学号/工号" />
        <input v-model="registerName" placeholder="姓名" />
        <input v-model="registerPassword" type="password" placeholder="密码" />
        <input v-model="registerPasswordConfirm" type="password" placeholder="确认密码" />
        <button @click="doRegister">注册</button>
        <p class="msg">{{ registerMessage }}</p>
      </div>
    </div>

    <!-- 学生菜单 -->
    <div v-else-if="user && user.role === 'STUDENT'" class="menu-card">
      <h3>🎓 学生菜单</h3>
      <div class="button-group">
        <button @click="showBindDorm = true">绑定/修改宿舍</button>
        <button @click="showCheckMe = true">查看个人信息</button>
        <button @click="showCreateRepair = true">创建报修单</button>
        <button @click="loadMyRepairs">📋 查看我的报修记录</button>
        <button @click="showCancelRepair = true">取消报修单</button>
        <button @click="showChangePassword = true">修改密码</button>
        <button class="logout" @click="logout">退出登录</button>
      </div>
      <!-- 报修记录列表 -->
      <div v-if="myRepairs.length" class="repair-list">
        <h4>我的报修单</h4>
        <table>
          <thead>
          <tr>
            <th>单号</th>
            <th>设备</th>
            <th>状态</th>
            <th>创建时间</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="r in myRepairs" :key="r.orderId">
            <td>{{ r.orderId }}</td>
            <td>{{ r.deviceType || '-' }}</td>
            <td>{{ r.status }}</td>
            <td>{{ formatDate(r.createTime) }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 管理员菜单 -->
    <div v-else-if="user && user.role === 'ADMIN'" class="menu-card">
      <h3>⚙️ 管理员菜单</h3>
      <div class="button-group">
        <button @click="loadAllRepairs">📋 查看所有报修单</button>
        <button @click="showCheckCertain = true">查看报修单详情</button>
        <button @click="showUpdateStatus = true">更新报修单状态</button>
        <button @click="showDeleteRepair = true">删除报修单</button>
        <button @click="showChangePassword = true">修改密码</button>
        <button class="logout" @click="logout">退出登录</button>
      </div>
      <!-- 报修单列表 -->
      <div v-if="allRepairs.length" class="repair-list">
        <div class="filter-bar">
          <label>按状态筛选：</label>
          <select v-model="statusFilter">
            <option value="">全部</option>
            <option value="PENDING">未处理</option>
            <option value="PROCESSING">正在处理</option>
            <option value="COMPLETED">处理完成</option>
            <option value="CANCELED">已取消</option>
          </select>
          <button @click="filterRepairs">筛选</button>
        </div>
        <table>
          <thead>
          <tr>
            <th>单号</th>
            <th>学号</th>
            <th>设备</th>
            <th>状态</th>
            <th>创建时间</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="r in filteredRepairs" :key="r.orderId">
            <td>{{ r.orderId }}</td>
            <td>{{ r.id }}</td>
            <td>{{ r.deviceType || '-' }}</td>
            <td>{{ r.status }}</td>
            <td>{{ formatDate(r.createTime) }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

      <!-- 各种弹窗（简化版，用 window.prompt 或简单的模态框） -->
      <div v-if="showBindDorm" class="modal-overlay" @click.self="showBindDorm=false">
        <div class="modal">
          <h4>绑定/修改宿舍</h4>
          <input v-model="dormBuilding" placeholder="宿舍楼" />
          <input v-model="roomNumber" placeholder="房间号" />
          <button @click="submitBindDorm">提交</button>
          <button @click="showBindDorm=false">取消</button>
          <p>{{ bindDormMsg }}</p>
        </div>
      </div>

      <div v-if="showCheckMe" class="modal-overlay" @click.self="showCheckMe=false">
        <div class="modal">
          <h4>个人信息</h4>
          <p>学号：{{ user?.id }}</p>
          <p>姓名：{{ user?.name }}</p>
          <p>角色：{{ user?.role === 'STUDENT' ? '学生' : '管理员' }}</p>
          <p>宿舍：{{ user?.dormBuilding || '未绑定' }} {{ user?.roomNumber || '' }}</p>
          <button @click="showCheckMe=false">关闭</button>
        </div>
      </div>

      <div v-if="showCreateRepair" class="modal-overlay" @click.self="showCreateRepair=false">
        <div class="modal">
          <h4>创建报修单</h4>
          <input v-model="phoneNumber" placeholder="手机号" />
          <input v-model="deviceType" placeholder="设备类型" />
          <textarea v-model="descriptionText" placeholder="问题描述"></textarea>
          <button @click="submitCreateRepair">提交</button>
          <button @click="showCreateRepair=false">取消</button>
          <p>{{ createRepairMsg }}</p>
        </div>
      </div>

      <div v-if="showCancelRepair" class="modal-overlay" @click.self="showCancelRepair=false">
        <div class="modal">
          <h4>取消报修单</h4>
          <input v-model="cancelOrderId" placeholder="报修单号" />
          <button @click="submitCancelRepair">取消报修</button>
          <button @click="showCancelRepair=false">取消</button>
          <p>{{ cancelMsg }}</p>
        </div>
      </div>

      <div v-if="showChangePassword" class="modal-overlay" @click.self="showChangePassword=false">
        <div class="modal">
          <h4>修改密码</h4>
          <input v-model="oldPassword" type="password" placeholder="旧密码" />
          <input v-model="newPassword" type="password" placeholder="新密码" />
          <input v-model="confirmPassword" type="password" placeholder="确认新密码" />
          <button @click="submitChangePassword">提交</button>
          <button @click="showChangePassword=false">取消</button>
          <p>{{ changePwdMsg }}</p>
        </div>
      </div>

      <div v-if="showCheckCertain" class="modal-overlay" @click.self="showCheckCertain=false">
        <div class="modal">
          <h4>查看报修单详情</h4>
          <input v-model="detailOrderId" placeholder="报修单号" />
          <button @click="loadRepairDetail">查询</button>
          <pre v-if="repairDetail">{{ repairDetail }}</pre>
          <button @click="showCheckCertain=false">关闭</button>
        </div>
      </div>

      <div v-if="showUpdateStatus" class="modal-overlay" @click.self="showUpdateStatus=false">
        <div class="modal">
          <h4>更新报修单状态</h4>
          <input v-model="updateOrderId" placeholder="报修单号" />
          <select v-model="newStatus">
            <option value="PROCESSING">正在处理</option>
            <option value="COMPLETED">处理完成</option>
          </select>
          <button @click="submitUpdateStatus">更新</button>
          <button @click="showUpdateStatus=false">取消</button>
          <p>{{ updateStatusMsg }}</p>
        </div>
      </div>

      <div v-if="showDeleteRepair" class="modal-overlay" @click.self="showDeleteRepair=false">
        <div class="modal">
          <h4>删除报修单</h4>
          <input v-model="deleteOrderId" placeholder="报修单号" />
          <button @click="submitDeleteRepair">删除</button>
          <button @click="showDeleteRepair=false">取消</button>
          <p>{{ deleteMsg }}</p>
        </div>
      </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

// axios 拦截器：自动携带 token
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// 全局状态
const loggedIn = ref(false)
const user = ref(null)

// 登录表单
const loginId = ref('')
const loginPassword = ref('')
const message = ref('')

// 注册表单
const activeTab = ref('login')
const registerRole = ref('STUDENT')
const registerId = ref('')
const registerName = ref('')
const registerPassword = ref('')
const registerPasswordConfirm = ref('')
const registerMessage = ref('')

// 学生数据
const myRepairs = ref([])
const showBindDorm = ref(false)
const showCheckMe = ref(false)
const showCreateRepair = ref(false)
const showCancelRepair = ref(false)
const showChangePassword = ref(false)

const dormBuilding = ref('')
const roomNumber = ref('')
const bindDormMsg = ref('')

const phoneNumber = ref('')
const deviceType = ref('')
const descriptionText = ref('')
const createRepairMsg = ref('')

const cancelOrderId = ref('')
const cancelMsg = ref('')

const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const changePwdMsg = ref('')

// 管理员数据
const allRepairs = ref([])
const filteredRepairs = ref([])
const statusFilter = ref('')
const showCheckCertain = ref(false)
const showUpdateStatus = ref(false)
const showDeleteRepair = ref(false)

const detailOrderId = ref('')
const repairDetail = ref(null)

const updateOrderId = ref('')
const newStatus = ref('PROCESSING')
const updateStatusMsg = ref('')

const deleteOrderId = ref('')
const deleteMsg = ref('')

// ---------- 辅助函数 ----------
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${d.getMonth()+1}-${d.getDate()} ${d.getHours()}:${d.getMinutes()}`
}

// ---------- 登录 ----------
const doLogin = async () => {
  try {
    const res = await axios.post('/api/login', { id: loginId.value, password: loginPassword.value })
    if (res.data.code === 200) {
      const { token, user: userData } = res.data.data
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(userData))
      user.value = userData
      loggedIn.value = true
      message.value = ''
      // 登录后自动加载对应列表
      if (userData.role === 'STUDENT') await loadMyRepairs()
      else await loadAllRepairs()
    } else {
      message.value = res.data.message
    }
  } catch (err) {
    message.value = '登录失败，请检查网络'
  }
}

// ---------- 注册 ----------
const doRegister = async () => {
  if (registerPassword.value !== registerPasswordConfirm.value) {
    registerMessage.value = '两次密码输入不一致'
    return
  }
  try {
    const payload = {
      id: registerId.value,
      name: registerName.value,
      password: registerPassword.value,
      role: registerRole.value
    }
    const res = await axios.post('/api/register', payload)
    if (res.data.code === 200) {
      registerMessage.value = '注册成功，请登录'
      // 清空表单
      registerId.value = ''
      registerName.value = ''
      registerPassword.value = ''
      registerPasswordConfirm.value = ''
      activeTab.value = 'login'
    } else {
      registerMessage.value = res.data.message
    }
  } catch (err) {
    registerMessage.value = '注册失败，请稍后重试'
  }
}

// ---------- 学生功能 ----------
const loadMyRepairs = async () => {
  try {
    const res = await axios.get('/api/user/myrepairorder')
    myRepairs.value = res.data.data || []
  } catch (err) {
    console.error('加载报修单失败', err)
  }
}

const submitBindDorm = async () => {
  try {
    const res = await axios.put('/api/user/update', { dormBuilding: dormBuilding.value, roomNumber: roomNumber.value })
    if (res.data.code === 200) {
      bindDormMsg.value = '宿舍绑定成功'
      // 更新本地用户信息
      const curUser = JSON.parse(localStorage.getItem('user'))
      curUser.dormBuilding = dormBuilding.value
      curUser.roomNumber = roomNumber.value
      localStorage.setItem('user', JSON.stringify(curUser))
      user.value = curUser
      setTimeout(() => { showBindDorm.value = false; bindDormMsg.value = '' }, 1500)
    } else {
      bindDormMsg.value = res.data.message
    }
  } catch (err) {
    bindDormMsg.value = '操作失败'
  }
}

const submitCreateRepair = async () => {
  try {
    const res = await axios.post('/api/user/callfix', {
      phoneNumber: phoneNumber.value,
      deviceType: deviceType.value,
      descriptionText: descriptionText.value
    })
    if (res.data.code === 200) {
      createRepairMsg.value = '创建成功'
      await loadMyRepairs()
      setTimeout(() => { showCreateRepair.value = false; createRepairMsg.value = '' }, 1500)
    } else {
      createRepairMsg.value = res.data.message
    }
  } catch (err) {
    createRepairMsg.value = '创建失败'
  }
}

const submitCancelRepair = async () => {
  try {
    const res = await axios.put(`/api/${cancelOrderId.value}/cancel`)
    if (res.data.code === 200) {
      cancelMsg.value = '取消成功'
      await loadMyRepairs()
      setTimeout(() => { showCancelRepair.value = false; cancelMsg.value = '' }, 1500)
    } else {
      cancelMsg.value = res.data.message
    }
  } catch (err) {
    cancelMsg.value = '取消失败'
  }
}

const submitChangePassword = async () => {
  if (newPassword.value !== confirmPassword.value) {
    changePwdMsg.value = '两次新密码不一致'
    return
  }
  try {
    const res = await axios.put('/api/changepassword', {
      oldPassword: oldPassword.value,
      newPassword: newPassword.value
    })
    if (res.data.code === 200) {
      changePwdMsg.value = '修改成功'
      setTimeout(() => { showChangePassword.value = false; changePwdMsg.value = '' }, 1500)
    } else {
      changePwdMsg.value = res.data.message
    }
  } catch (err) {
    changePwdMsg.value = '修改失败'
  }
}

// ---------- 管理员功能 ----------
const loadAllRepairs = async () => {
  try {
    const res = await axios.get('/api/administer/select')
    allRepairs.value = res.data.data || []
    filteredRepairs.value = allRepairs.value
  } catch (err) {
    console.error('加载报修单失败', err)
  }
}

const filterRepairs = () => {
  if (!statusFilter.value) {
    filteredRepairs.value = allRepairs.value
  } else {
    filteredRepairs.value = allRepairs.value.filter(r => r.status === statusFilter.value)
  }
}

const loadRepairDetail = async () => {
  try {
    const res = await axios.get(`/api/thisrepairorder/${detailOrderId.value}`)
    if (res.data.code === 200) {
      repairDetail.value = res.data.data
    } else {
      alert(res.data.message)
    }
  } catch (err) {
    alert('查询失败')
  }
}

const submitUpdateStatus = async () => {
  try {
    const res = await axios.put(`/api/${updateOrderId.value}/restatus?status=${newStatus.value}`)
    if (res.data.code === 200) {
      updateStatusMsg.value = '更新成功'
      await loadAllRepairs()
      setTimeout(() => { showUpdateStatus.value = false; updateStatusMsg.value = '' }, 1500)
    } else {
      updateStatusMsg.value = res.data.message
    }
  } catch (err) {
    updateStatusMsg.value = '更新失败'
  }
}

const submitDeleteRepair = async () => {
  try {
    const res = await axios.delete(`/api/${deleteOrderId.value}/delete`)
    if (res.data.code === 200) {
      deleteMsg.value = '删除成功'
      await loadAllRepairs()
      setTimeout(() => { showDeleteRepair.value = false; deleteMsg.value = '' }, 1500)
    } else {
      deleteMsg.value = res.data.message
    }
  } catch (err) {
    deleteMsg.value = '删除失败'
  }
}

// ---------- 退出登录 ----------
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  loggedIn.value = false
  user.value = null
  loginId.value = ''
  loginPassword.value = ''
}
</script>

<style scoped>
.title {
  text-align: center;
  margin-bottom: 24px;
}
.auth-card, .menu-card {
  background: #f9f9f9;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.tab-bar {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-bottom: 24px;
}
.tab-bar button {
  flex: 0 1 120px;
  padding: 8px;
  font-size: 16px;
  border: none;
  background: #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
}
.tab-bar button.active {
  background-color: #337a8f;
  color: white;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.form input, .form select, .form textarea {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
  width: 100%;
  box-sizing: border-box;
}
.form button {
  padding: 10px;
  background-color: #4ab1d1;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.msg {
  text-align: center;
  color: #d9534f;
}
.button-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;
  margin-bottom: 24px;
}
.button-group button {
  padding: 8px 16px;
  background-color: #429db9;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}
.button-group button.logout {
  background-color: #dc3545;
}
.repair-list {
  margin-top: 24px;
  overflow-x: auto;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}
th {
  background-color: #f2f2f2;
}
.filter-bar {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 16px;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal {
  background: white;
  padding: 20px;
  border-radius: 12px;
  min-width: 300px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.modal input, .modal select, .modal textarea {
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.modal button {
  padding: 8px;
  background: #429db9;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.modal pre {
  background: #f4f4f4;
  padding: 8px;
  max-height: 200px;
  overflow: auto;
}
</style>