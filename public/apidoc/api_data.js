define({ "api": [
  {
    "type": "delete",
    "url": "/courses/:id",
    "title": "删除",
    "version": "1.0.0",
    "name": "DeleteCourse",
    "group": "Course",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},         // 删除的课程的所有信息\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},           // 空对象\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/course.js",
    "groupTitle": "课程"
  },
  {
    "type": "get",
    "url": "/courses",
    "title": "获取所有",
    "version": "1.0.0",
    "name": "GetCourse",
    "group": "Course",
    "description": "<p>仅供调试。</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],           // 所有课程\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],           // 空数组\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/course.js",
    "groupTitle": "课程"
  },
  {
    "type": "post",
    "url": "/courses",
    "title": "创建",
    "version": "1.0.0",
    "name": "PostCourse",
    "group": "Course",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>课程名称</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "startTime",
            "description": "<p>开始时间（格式：2016-06-11）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "endTime",
            "description": "<p>结束时间（格式：2016-06-11）</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},           // 创建后的课程信息\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},           // 空对象\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/course.js",
    "groupTitle": "课程"
  },
  {
    "type": "post",
    "url": "/courses/search",
    "title": "查询",
    "version": "1.0.0",
    "name": "PostCourseSearch",
    "group": "Course",
    "description": "<p>根据指定字段名和值查询课程。（必须要有查询条件）</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "_id",
            "description": "<p>课程id（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>课程名称（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "teacherId",
            "description": "<p>所属教师id（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "location",
            "description": "<p>上课地点（可选）（excel导入）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "academy",
            "description": "<p>学院（可选）（excel导入）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "time",
            "description": "<p>上课时间（可选）（excel导入）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "startTime",
            "description": "<p>课程起始时间（创建时指定）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "endTime",
            "description": "<p>课程结束时间（创建时指定）</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "studentCount",
            "description": "<p>学生数量</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "signCount",
            "description": "<p>签到次数</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "state",
            "description": "<p>课程状态（0：未开始，1：进行中，2：已结束）</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],           // 查询到的所有课程\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],           // 空数组\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/course.js",
    "groupTitle": "课程"
  },
  {
    "type": "get",
    "url": "/publickey",
    "title": "获取公钥",
    "version": "1.0.0",
    "name": "GetPublickey",
    "group": "Key",
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n   code: \"200\",\n   data: \"pem格式的公钥\",\n   msg:  \"请求成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: \"\",\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/publickey.js",
    "groupTitle": "密钥"
  },
  {
    "type": "get",
    "url": "/signs",
    "title": "获取所有",
    "version": "1.0.0",
    "name": "GetSign",
    "group": "Sign",
    "description": "<p>仅供调试。</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],         // 查询到的所有签到\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],         // 空数组\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/sign.js",
    "groupTitle": "签到"
  },
  {
    "type": "post",
    "url": "/signs",
    "title": "创建",
    "version": "1.0.0",
    "name": "PostSign",
    "group": "Sign",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "courseId",
            "description": "<p>所属课程id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "startTime",
            "description": "<p>开始时间（格式：2016-06-11 10:00）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "endTime",
            "description": "<p>结束时间（格式：2016-06-11 11:00）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "color",
            "description": "<p>签到提醒颜色</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "relatedId",
            "description": "<p>关联指定id的课程的学生</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},         // 创建后的签到信息\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},         // 空对象\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/sign.js",
    "groupTitle": "签到"
  },
  {
    "type": "get",
    "url": "/smsCode/state?smsid=XXXX",
    "title": "查询状态",
    "version": "1.0.0",
    "name": "GetSmsCodeState",
    "group": "SmsCode",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "smsid",
            "description": "<p>成功发送短信验证码后收到的id</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {\n    sendState: 'SUCCESS',     // 发送状态，SENDING-发送中，FAIL-发送失败，SUCCESS-发送成功\n    verifyState: true         // 验证状态，true-已验证 false-未验证\n  },\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"3000\",\n  data: \"\",\n  msg:  \"验证码错误或者已被验证\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/smscode.js",
    "groupTitle": "短信验证码"
  },
  {
    "type": "post",
    "url": "/smsCode",
    "title": "发送",
    "version": "1.0.0",
    "name": "PostSmsCode",
    "group": "SmsCode",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>要发送短信验证码的手机号</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: \"1234567\",      // smsid，可用于查询短信发送状态\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"1004\",\n  data: \"\",             \n  msg:  \"手机号码无效\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/smscode.js",
    "groupTitle": "短信验证码"
  },
  {
    "type": "post",
    "url": "/smsCode/verification",
    "title": "验证",
    "version": "1.0.0",
    "name": "PostSmscodeVerification",
    "group": "SmsCode",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>要验证短信验证码的手机号</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "smsCode",
            "description": "<p>待验证的验证码</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: \"\",             \n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"3000\",\n  data: \"\",\n  msg:  \"验证码错误或者已被验证\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/smscode.js",
    "groupTitle": "短信验证码"
  },
  {
    "type": "get",
    "url": "/students",
    "title": "获取所有",
    "version": "1.0.0",
    "name": "GetStudent",
    "group": "Student",
    "description": "<p>仅供调试。</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],         // 查询到的所有学生\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],         // 空数组\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/students.js",
    "groupTitle": "学生"
  },
  {
    "type": "get",
    "url": "/students/images/:id",
    "title": "获取默认头像url",
    "version": "1.0.0",
    "name": "GetStudentImage",
    "group": "Student",
    "description": "<p>id可取值为1、2、3、4，代表4张不同的默认图片</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "code",
            "description": "<p>200表示成功。</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "data",
            "description": "<p>头像url</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: \"http://linkdust.xicp.net:50843/images/user/1.png\",    // 头像url\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: \"\",         \n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/students.js",
    "groupTitle": "学生"
  },
  {
    "type": "post",
    "url": "/students/images",
    "title": "上传头像",
    "version": "1.0.0",
    "name": "PostStudentImage",
    "group": "Student",
    "header": {
      "examples": [
        {
          "title": "请求头部",
          "content": "{\n   \"Content-Type\": \"multipart/form-data\"\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: \"http://linkdust.xicp.net:50843/images/XXX.png\",      // 头像url\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: \"\",         \n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/students.js",
    "groupTitle": "学生"
  },
  {
    "type": "post",
    "url": "/students/search",
    "title": "查询",
    "version": "1.0.0",
    "name": "PostStudentSearch",
    "group": "Student",
    "description": "<p>根据指定字段名和值查询学生。（必须要有查询条件）</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "_id",
            "description": "<p>学生id（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>手机号码（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "number",
            "description": "<p>学号（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>姓名（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "gender",
            "description": "<p>性别（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "school",
            "description": "<p>学校（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "academy",
            "description": "<p>学院（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "major",
            "description": "<p>专业（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "grade",
            "description": "<p>年级（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "class",
            "description": "<p>班级（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "mail",
            "description": "<p>邮箱（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "avatar",
            "description": "<p>头像url（可选）</p>"
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "code",
            "description": "<p>200表示成功。</p>"
          },
          {
            "group": "Success 200",
            "type": "Array",
            "optional": false,
            "field": "data",
            "description": "<p>符合条件的所有学生</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],         // 查询到的所有学生\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],         // 空数组\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/students.js",
    "groupTitle": "学生"
  },
  {
    "type": "put",
    "url": "/students/:id",
    "title": "修改",
    "version": "1.0.0",
    "name": "PutStudent",
    "group": "Student",
    "description": "<p>根据指定字段名和值修改指定id的学生</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "code",
            "description": "<p>200表示成功。</p>"
          },
          {
            "group": "Success 200",
            "type": "Object",
            "optional": false,
            "field": "data",
            "description": "<p>修改后的学生信息</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},         // 修改后的学生信息\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "number",
            "description": "<p>学号（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>姓名（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "gender",
            "description": "<p>性别（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "school",
            "description": "<p>学校（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "academy",
            "description": "<p>学院（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "major",
            "description": "<p>专业（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "grade",
            "description": "<p>年级（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "class",
            "description": "<p>班级（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "mail",
            "description": "<p>邮箱（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "avatar",
            "description": "<p>头像url（可选）</p>"
          }
        ]
      }
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},         // 空对象\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/students.js",
    "groupTitle": "学生"
  },
  {
    "type": "get",
    "url": "/teachers",
    "title": "获取所有",
    "version": "1.0.0",
    "name": "GetTeacher",
    "group": "Teacher",
    "description": "<p>仅供调试。</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],         // 所有的教师\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],         // 空数组\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/teachers.js",
    "groupTitle": "教师"
  },
  {
    "type": "post",
    "url": "/teachers/search",
    "title": "查询",
    "version": "1.0.0",
    "name": "PostTeacherSearch",
    "group": "Teacher",
    "description": "<p>根据指定字段名和值查询教师。（必须要有查询条件）</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "_id",
            "description": "<p>教师id（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>手机号码（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>姓名（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "school",
            "description": "<p>学校（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "academy",
            "description": "<p>学院（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "avatar",
            "description": "<p>头像url（可选）</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],         // 查询到的所有教师\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],         // 空数组\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/teachers.js",
    "groupTitle": "教师"
  },
  {
    "type": "post",
    "url": "/users",
    "title": "注册",
    "version": "1.0.0",
    "name": "PostUser",
    "group": "User",
    "description": "<p>注册成功，返回的响应头部的cookie中包含有后续请求所需要的access_token。</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>用户手机号码</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>密码（必须用服务器提供的公钥进行加密）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "role",
            "description": "<p>角色（“0”表示学生，“1”表示教师）</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},         // 注册成功的用户的信息\n  msg:  \"请求成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},         // 空对象\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/users.js",
    "groupTitle": "用户"
  },
  {
    "type": "post",
    "url": "/users/login",
    "title": "密码登录",
    "version": "1.0.0",
    "name": "PostUserLogin",
    "group": "User",
    "description": "<p>登录成功，返回的响应头部的cookie中包含有后续请求所需要的access_token。</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>用户手机号码</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "password",
            "description": "<p>密码（必须用服务器提供的公钥进行加密）</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {}          // 登录成功的用户的信息\n  msg:  \"请求成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},         // 空对象\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/users.js",
    "groupTitle": "用户"
  },
  {
    "type": "post",
    "url": "/users/loginWithSmsCode",
    "title": "验证码登录",
    "version": "1.0.0",
    "name": "PostUserLoginWithSmsCode",
    "group": "User",
    "description": "<p>登录成功，返回的响应头部的cookie中包含有后续请求所需要的access_token。</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>用户手机号码</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "smsCode",
            "description": "<p>验证码</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},         // 登录成功的用户的信息\n  msg:  \"请求成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},         // 空对象\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/users.js",
    "groupTitle": "用户"
  },
  {
    "type": "post",
    "url": "/users/search",
    "title": "查询",
    "version": "1.0.0",
    "name": "PostUserSearch",
    "group": "User",
    "description": "<p>根据指定字段名和值查询用户，返回的用户对象不包含密码字段。</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "_id",
            "description": "<p>用户id（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>用户手机号码（可选）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "role",
            "description": "<p>用户角色（可选）</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],         // 查询到的所有用户\n  msg:  \"请求成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],         // 空数组\n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/users.js",
    "groupTitle": "用户"
  }
] });
