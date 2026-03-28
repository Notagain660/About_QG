参考：廖雪峰的git教程和deepseek


建空仓库：不要添加read me，represitory全部不勾选

配置身份
git config --global user.name "你的GitHub用户名"
git config --global user.email "你的GitHub邮箱"

打开目标文件夹
`cd "D:/Users/starter······"`

初始化仓库
git init

添加文件
git add .（文件夹全部，非全部则输入文件名）
上传本地仓库

git commit -m "附注”
关联远程仓库

git remote add origin https://github.com/你的用户名/仓库名.git

初次提交
git branch -M main
git push -u origin main

强制推送
`git push -u origin main --force

非初次
git push

设置网络代理
git config --global http.proxy http://127.0.0.1:端口
git config --global https.proxy http://127.0.0.1:端口

测试代理是否工作
 `curl -x http://127.0.0.1:端口地址 https://github.com -I`测试代理是否工作

取消代理
git config --global --unset http.proxy
git config --global --unset https.proxy

查看状态
git status

查看本地日志
git log --oneline --decorate --graph --all

删除远程仓库
`git remote rm origin        # 删除旧的`
`git remote add origin 新URL # 添加新的`
删除本地仓库
`rm -rf .git`


###  删除锁文件

在 Git Bash 中执行：

bash

rm -f /d/Users/starter/aboutqg/.git/index.lock

注意路径：锁文件在仓库根目录的 `.git` 文件夹下。根据你之前的信息，仓库根目录是 `D:/Users/starter/aboutqg`（因为你当前在 `week01` 下，但 `.git` 在上级目录）。如果你不确定，可以先切换到仓库根目录：

bash

cd /d/Users/starter/aboutqg
rm -f .git/index.lock

### 3️⃣ 重新回到 `week01` 目录，并恢复工作区

bash

cd week01
git reset --hard HEAD

现在应该能正常恢复到上一次提交的状态。



`find` 搜索 `pom.xml` 来定位：

bash

find . -name "pom.xml" -type f

1. **添加整个 `demo` 文件夹**：
    
    bash
    
    git add week01/project/demo


d /d/Users/starter/aboutqg
pwd   # 确认在 aboutqg

---

## 📁 检查目录结构

bash

ls week01/project/

git rm -r week01/project清理


- 将 `originalwork` 移到 `week01` 下并重命名为 `firstweek`：
    
    bash
    
    mv week01/project/originalwork week01/firstweek



查看该目录下的内容：

bash

ls -la


检查 Git 能否连接 GitHub：

bash

git ls-remote origin

正常输出：From https://github.com/Notagain660/About_QG.git
d719674b007408d541558bc2fd6bad06fd0d642d        HEAD
d719674b007408d541558bc2fd6bad06fd0d642d        refs/heads/main



**测试代理是否生效**：

bash

curl -I https://github.com


git log --oneline -3

确保有未推送的提交。