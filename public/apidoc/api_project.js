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
    "content": "<h2><strong>带有access_token的response示例</strong></h2>\n<p><img src=\"http://linkdust.xicp.net:50843/images/response_demo.png\" alt=\"image\"></p>\n<h2><strong>操作码表</strong></h2>\n<table>\n<thead>\n<tr>\n<th>操作码</th>\n<th>相关信息</th>\n</tr>\n</thead>\n<tbody>\n<tr>\n<td>1000</td>\n<td>手机号码不能为空</td>\n</tr>\n<tr>\n<td>1001</td>\n<td>密码不能为空</td>\n</tr>\n<tr>\n<td>1002</td>\n<td>角色不能为空</td>\n</tr>\n<tr>\n<td>1003</td>\n<td>用户角色的值只能为&quot;0&quot;或&quot;1&quot;</td>\n</tr>\n<tr>\n<td>1004</td>\n<td>手机号码无效</td>\n</tr>\n<tr>\n<td>1005</td>\n<td>密码解密失败</td>\n</tr>\n<tr>\n<td>1006</td>\n<td>学号不能为空</td>\n</tr>\n<tr>\n<td>1007</td>\n<td>课程名称不能为空</td>\n</tr>\n<tr>\n<td>1008</td>\n<td>查询条件不能为空</td>\n</tr>\n<tr>\n<td>1009</td>\n<td>课程状态的值只能为0、1、2</td>\n</tr>\n<tr>\n<td>1010</td>\n<td>课程起始时间不能为空</td>\n</tr>\n<tr>\n<td>1011</td>\n<td>课程结束时间不能为空</td>\n</tr>\n<tr>\n<td>1012</td>\n<td>课程起始时间格式不正确</td>\n</tr>\n<tr>\n<td>1013</td>\n<td>课程结束时间格式不正确</td>\n</tr>\n<tr>\n<td>1014</td>\n<td>课程开始时间要在结束时间之前</td>\n</tr>\n<tr>\n<td>2000</td>\n<td>手机号码或密码不能为空</td>\n</tr>\n<tr>\n<td>2001</td>\n<td>手机号码或密码错误</td>\n</tr>\n<tr>\n<td>2002</td>\n<td>该手机号码已被注册</td>\n</tr>\n<tr>\n<td>2003</td>\n<td>用户不存在</td>\n</tr>\n<tr>\n<td>3000</td>\n<td>验证码错误或者已被验证</td>\n</tr>\n<tr>\n<td>3001</td>\n<td>该验证码尚未被验证</td>\n</tr>\n<tr>\n<td>3002</td>\n<td>该号码已达到短信验证码发送上限</td>\n</tr>\n<tr>\n<td>3004</td>\n<td>无效的smsid</td>\n</tr>\n<tr>\n<td>401</td>\n<td>认证信息有误</td>\n</tr>\n<tr>\n<td>402</td>\n<td>认证信息已失效</td>\n</tr>\n<tr>\n<td>403</td>\n<td>无权限访问该资源</td>\n</tr>\n<tr>\n<td>500</td>\n<td>内部服务器错误</td>\n</tr>\n<tr>\n<td>200</td>\n<td>操作成功</td>\n</tr>\n</tbody>\n</table>\n"
  },
  "order": [
    "Key",
    "User",
    "PostUser",
    "PostUserLogin",
    "PostUserLoginWithSmsCode",
    "SmsCode",
    "PostSmsCode",
    "PostSmsCodeVerification",
    "PostSmsCodeState",
    "Student",
    "GetStudent",
    "PutStudent",
    "PostStudentSearch",
    "PostStudentImage",
    "Teacher",
    "GetTeacher",
    "PostTeacherSearch",
    "Course",
    "GetCourse",
    "PostCourse",
    "DeleteCourse",
    "PostCourseSearch",
    "Sign",
    "GetSign",
    "PostSign"
  ],
  "template": {
    "withGenerator": false
  },
  "sampleUrl": false,
  "apidoc": "0.2.0",
  "generator": {
    "name": "apidoc",
    "time": "2016-09-19T08:21:27.859Z",
    "url": "http://apidocjs.com",
    "version": "0.16.1"
  }
});
