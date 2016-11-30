define({
  "name": "华师小签后台API",
  "version": "1.0.0",
  "description": "为华师小签各个终端提供资源的Restful api服务",
  "title": "title",
  "url": "http://linkdust.xicp.net:50843/api",
  "header": {
    "title": "说明",
    "content": "<p>一切请求，如果服务器有响应，则会返回如下json数据：</p>\n<pre><code>{\n  code: &quot;操作码&quot;,\n  data: &quot;数据&quot;,\n  msg:  &quot;错误信息&quot;\n}\n</code></pre>\n<p>code值为“200”时，表明操作成功，其他情况均为失败。</p>\n<p>所有操作码与对应信息在<a href=\"#api-_footer\">文档末尾处</a>可以查看。</p>\n"
  },
  "footer": {
    "title": "操作码表",
    "content": "<h2><strong>带有access_token的response示例</strong></h2>\n<p><img src=\"http://linkdust.xicp.net:50843/images/response_demo.png\" alt=\"image\"></p>\n<h2><strong>操作码表</strong></h2>\n<table>\n<thead>\n<tr>\n<th>操作码</th>\n<th>相关信息</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td>1000</td>\n<td>手机号码不能为空</td>\n</tr>\n<tr>\n<td>1001</td>\n<td>密码不能为空</td>\n</tr>\n<tr>\n<td>1002</td>\n<td>角色不能为空</td>\n</tr>\n<tr>\n<td>1003</td>\n<td>用户角色的值只能为&quot;0&quot;或&quot;1&quot;</td>\n</tr>\n<tr>\n<td>1004</td>\n<td>手机号码无效</td>\n</tr>\n<tr>\n<td>1005</td>\n<td>密码解密失败</td>\n</tr>\n<tr>\n<td>1006</td>\n<td>学号不能为空</td>\n</tr>\n<tr>\n<td>1007</td>\n<td>课程名称不能为空</td>\n</tr>\n<tr>\n<td>1008</td>\n<td>查询条件不能为空</td>\n</tr>\n<tr>\n<td>1009</td>\n<td>签到类型的值只能为0、1、2</td>\n</tr>\n<tr>\n<td>1015</td>\n<td>服务器没接收到上传的文件</td>\n</tr>\n<tr>\n<td>1016</td>\n<td>不存在该默认图片</td>\n</tr>\n<tr>\n<td>1017</td>\n<td>签到记录的状态的值只能为0、1、2</td>\n</tr>\n<tr>\n<td>1018</td>\n<td>签到记录的类型的值只能为0、1</td>\n</tr>\n<tr>\n<td>2000</td>\n<td>手机号码或密码不能为空</td>\n</tr>\n<tr>\n<td>2001</td>\n<td>手机号码或密码错误</td>\n</tr>\n<tr>\n<td>2002</td>\n<td>该手机号码已被注册</td>\n</tr>\n<tr>\n<td>2003</td>\n<td>用户不存在</td>\n</tr>\n<tr>\n<td>3000</td>\n<td>验证码错误或者已被验证</td>\n</tr>\n<tr>\n<td>3001</td>\n<td>该验证码尚未被验证</td>\n</tr>\n<tr>\n<td>3002</td>\n<td>该号码已达到短信验证码发送上限</td>\n</tr>\n<tr>\n<td>3004</td>\n<td>无效的smsid</td>\n</tr>\n<tr>\n<td>4000</td>\n<td>签到失败，该签到不存在</td>\n</tr>\n<tr>\n<td>4001</td>\n<td>签到失败，该签到没有对应某个课程</td>\n</tr>\n<tr>\n<td>4002</td>\n<td>IP地址不能为空</td>\n</tr>\n<tr>\n<td>4003</td>\n<td>定位失败</td>\n</tr>\n<tr>\n<td>4004</td>\n<td>教师尚未定位，无法进行签到</td>\n</tr>\n<tr>\n<td>4005</td>\n<td>尚未设置学号，无法进行签到</td>\n</tr>\n<tr>\n<td>4006</td>\n<td>该学生不属于这门课程，无法进行签到</td>\n</tr>\n<tr>\n<td>4007</td>\n<td>签到尚未开始</td>\n</tr>\n<tr>\n<td>4008</td>\n<td>签到已结束</td>\n</tr>\n<tr>\n<td>5000</td>\n<td>课程不存在</td>\n</tr>\n<tr>\n<td>5001</td>\n<td>该课程暂无相关签到</td>\n</tr>\n<tr>\n<td>5002</td>\n<td>该课程暂无相关教师</td>\n</tr>\n<tr>\n<td>401</td>\n<td>认证信息有误</td>\n</tr>\n<tr>\n<td>402</td>\n<td>认证信息已失效</td>\n</tr>\n<tr>\n<td>403</td>\n<td>无权限访问该资源</td>\n</tr>\n<tr>\n<td>500</td>\n<td>内部服务器错误</td>\n</tr>\n<tr>\n<td>200</td>\n<td>操作成功</td>\n</tr>\n<tr>\n<td>1019</td>\n<td>查询日期不能为空</td>\n</tr>\n<tr>\n<td>4009</td>\n<td>该学生已经参与过本次签到</td>\n</tr>\n<tr>\n<td>4010</td>\n<td>该手机已经参与过本次签到</td>\n</tr>\n<tr>\n<td>4011</td>\n<td>该学生尚未注册</td>\n</tr>\n<tr>\n<td>4012</td>\n<td>相应课程没有导入该学生信息</td>\n</tr>\n<tr>\n<td>6000</td>\n<td>缺少发言者Id</td>\n</tr>\n<tr>\n<td>7000</td>\n<td>反馈内容不能为空</td>\n</tr>\n</tbody>\n</table>\n<h2>实时通讯</h2>\n<ol>\n<li>建立连接 url: http://linkdust.xicp.net:50843/sign</li>\n<li>客户端监听事件（签到）\n<ul>\n<li>notice</li>\n</ul>\n</li>\n<li>客户端触发事件（签到）\n<ul>\n<li>student-in</li>\n<li>student-out</li>\n</ul>\n</li>\n<li>客户端监听事件（聊天室）\n<ul>\n<li>room-list</li>\n<li>msg-list</li>\n<li>new-msg</li>\n</ul>\n</li>\n<li>客户端触发事件（聊天室）\n<ul>\n<li>room-list</li>\n<li>msg-list</li>\n<li>new-msg</li>\n</ul>\n</li>\n</ol>\n<h2>事件描述</h2>\n<ol>\n<li>notice\n<ul>\n<li>每当学生的签到被教师处理时，会触发该事件</li>\n<li>参数：空字符串</li>\n</ul>\n</li>\n<li>student-in\n<ul>\n<li>客户端只有触发了该事件，才能接收到notice事件</li>\n<li>参数：studentId</li>\n</ul>\n</li>\n<li>student-out\n<ul>\n<li>客户端触发了该事件，则不会再收到notice事件</li>\n<li>参数：studentId</li>\n</ul>\n</li>\n<li>room-list\n<ul>\n<li>客户端触发该事件后，可以监听该事件，以获得相关聊天室信息</li>\n<li>触发参数：studentId, teacherId（选择其中一个参数进行设置，另一个参数为空字符串即可）</li>\n<li>接收参数：code, data, msg（与http返回结果一致，data则为聊天室信息）</li>\n<li>data 结构：\n<ul>\n<li>courseId 课程id</li>\n<li>name 聊天室名字（即课程名）</li>\n<li>avatar 聊天室头像（暂时为教师头像）</li>\n<li>count 参与聊天室的人数</li>\n<li>msg 最近一条信息（参考下述信息对象结构）</li>\n</ul>\n</li>\n</ul>\n</li>\n<li>msg-list\n<ul>\n<li>客户端触发该事件后，可以监听该事件，以获得特定聊天室的信息列表</li>\n<li>触发参数：courseId, page, limit（page默认0，limit默认18）</li>\n<li>接收参数：code, data, msg（与http返回结果一致，data则为信息列表）</li>\n<li>data 结构：信息对象数组，参考下述信息对象结构</li>\n</ul>\n</li>\n<li>new-msg\n<ul>\n<li>客户端想要发送信息，则触发该事件，监听该事件时则会实时获得他人发送的新信息，但不会接收到自己发送的新信息</li>\n<li>触发参数：courseId, studentId, content, teacherId（studentId, teacherId选择一个进行设置，另一个则为空字符串）</li>\n<li>接收参数：code, data, msg（与http返回结果一致，data则为新信息）</li>\n<li>data 结构：信息对象\n<ul>\n<li>courseId 课程id</li>\n<li>studentId 不为空，代表学生发送</li>\n<li>teacherId 不为空，代表教师发送</li>\n<li>content 信息内容</li>\n<li>avatar 信息发送者头像</li>\n<li>name 信息发送者姓名</li>\n<li>createdAt 信息发送时间</li>\n</ul>\n</li>\n</ul>\n</li>\n</ol>\n"
  },
  "order": [
    "Key",
    "User",
    "PostUser",
    "PostUserLogin",
    "PostUserLoginWithSmsCode",
    "PostUserSearch",
    "PutUser",
    "SmsCode",
    "PostSmsCode",
    "PostSmsCodeVerification",
    "PostSmsCodeState",
    "Student",
    "GetStudent",
    "PutStudent",
    "PostStudentSearch",
    "PostStudentImage",
    "GetStudentImage",
    "GetStudentRelatedCourses",
    "GetStudentNotice",
    "GetStudentActiveInfo",
    "GetStudentSignInDays",
    "GetStudentSignInDaysDetail",
    "GetStudentChatDays",
    "GetStudentChatDayDetail",
    "Teacher",
    "GetTeacher",
    "PostTeacherSearch",
    "Course",
    "GetCourse",
    "PostCourse",
    "DeleteCourse",
    "PostCourseSearch",
    "GetCourseLatestSignRecord",
    "GetCourseStudentSignRecord",
    "Sign",
    "GetSign",
    "PostSign",
    "GetSignScanning",
    "SignRecord",
    "GetSignRecord",
    "PostSignRecord",
    "PostSignRecordSearch",
    "Feedback",
    "GetFeedback",
    "PostFeedback"
  ],
  "template": {
    "withGenerator": false
  },
  "sampleUrl": false,
  "apidoc": "0.2.0",
  "generator": {
    "name": "apidoc",
    "time": "2016-11-30T07:49:37.792Z",
    "url": "http://apidocjs.com",
    "version": "0.16.1"
  }
});
