参考：廖雪峰的git教程和deepseek


#### 报错信息：
1. $ cd D:\Users\starter\untitled\src\
     >git init
     >   bash: cd: too many arguments
     >   
     >  **Git Bash** 中，**Windows 路径的反斜杠 `\` 会被识别为转义字符**，导致 `cd` 命令后面被拆分成多个参数，所以提示 `too many arguments`     
##### 方法一：在文件夹中右键打开 Git Bash（最推荐，最简单）
1. 打开文件资源管理器，找到你的项目文件夹 `D:\Users\starter\untitled\src\`
2. 在文件夹空白处**右键点击**，选择 **Git Bash Here**
3. 此时 Git Bash 会自动定位到该文件夹，直接输入 `git init` 即可
##### 方法二：手动输入路径时使用正斜杠并加引号
如果你坚持在打开的 Git Bash 中输入 `cd` 命令，请使用正斜杠 `/`，并用双引号把整个路径括起来（如果路径中有空格的话）：
bash
cd "D:/Users/starter/untitled/src"
注意：
- 路径中的反斜杠 `\` 要换成 **正斜杠 `/`**（Windows 也支持正斜杠）
- 路径末尾不要加 `\`，直接到文件夹名即可
- 如果你之前已经输入了错误命令，可以先按 **Ctrl+C** 取消当前行，重新输入。
##### 方法三：使用 `pwd` 查看当前位置，然后逐步跳转
如果你不清楚当前在哪个目录，可以用 `pwd` 查看。然后一级一级 `cd` 进入：
bash
pwd                 # 显示当前目录
cd ..               # 返回上一级
cd 目标文件夹名      # 注意：如果文件夹名有空格，也要加引号



2. warning: in the working copy of 'Main.java', LF will be replaced by CRLF the next time Git touches it
>Git 在告诉你：**你的文件 `Main.java` 里换行符是 LF（Linux/macOS 风格），而 Git 在 Windows 上默认会把它转成 CRLF（Windows 风格）**，下次 Git 处理这个文件（比如检出、提交）时会自动替换。


3. fatal: unable to access 'https://github.com/Notagain660/Studio-works.git/': Recv failure: Connection was reset
>通常是因为网络无法稳定连接到 GitHub。结合你所在的网络环境，大概率是连接问题
>方法：换网络/重启git bash/配置代理
>/临时关闭SSL验证（不推荐长期使用，仅测试）
      这是一个临时测试方案，用来确认是否是SSL证书验证导致的问题。在 Git Bash 中执行：
      git config --global http.sslVerify false
      然后再次尝试 `git push`。如果这次成功了，说明问题可能与SSL有关，但请记得在推送成功后，重新开启SSL验证：
      git config --global http.sslVerify true

4. fatal: unable to access 'https://github.com/Notagain660/1Weekwork.git/': Failed to connect to github.com port 443 after 21216 ms: Could not connect to server
    取消配置再试，试了不行就重新配置

5. error: remote origin already exists.提醒远程仓库已存在





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