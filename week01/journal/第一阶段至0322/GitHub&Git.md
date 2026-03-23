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