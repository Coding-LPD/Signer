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
    "type": "get",
    "url": "/courses/:id/latestSignRecords",
    "title": "最近签到记录",
    "version": "1.0.0",
    "name": "GetCourseLatestSignRecord",
    "group": "Course",
    "description": "<p>查询指定id的课程的最近签到记录，返回课程信息和最多20个最近签到者头像</p>",
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: \n  {\n    signNum: XXX        // 签到次数\n    course: \n    {\n      name: '',\n      time: '',\n      location: '',\n      teacherName: ''\n    }          \n    records:            // 最多20个最近签到记录\n    [{\n      _id: 'XXX',       // 签到记录id\n      name: '',         // 学生姓名\n      avatar: ''        // 学生头像url\n    }]         \n  },          \n  msg:  \"操作成功\"\n}",
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
    "url": "/courses/:id/students/:studentId/signRecords",
    "title": "指定学生的签到情况",
    "version": "1.0.0",
    "name": "GetCourseStudentSignRecord",
    "group": "Course",
    "description": "<p>查询指定studentId的学生在指定id的课程中，所有的签到情况</p>",
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: \n  [{\n    signId: '',       // 签到id\n    time: '',         // 签到日期（2016-09-10）\n    tag: ''           // 签到完成情况（true完成，false没完成）\n  }]          \n  msg:  \"操作成功\"\n}",
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
            "field": "time",
            "description": "<p>上课时间（格式：星期一 1节-4节,星期二 3节-4节）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "location",
            "description": "<p>上课地点</p>"
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
            "description": "<p>课程id</p>"
          },
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
            "field": "teacherId",
            "description": "<p>所属教师id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "location",
            "description": "<p>上课地点</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "academy",
            "description": "<p>学院（excel导入）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "time",
            "description": "<p>上课时间</p>"
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
    "url": "/feedbacks",
    "title": "获取所有",
    "version": "1.0.0",
    "name": "GetFeedback",
    "group": "Feedback",
    "description": "<p>仅供调试。</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],         // 查询到的所有反馈\n  msg:  \"操作成功\"\n}",
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
    "filename": "docjs/feedback.js",
    "groupTitle": "反馈"
  },
  {
    "type": "post",
    "url": "/feedbacks",
    "title": "创建",
    "version": "1.0.0",
    "name": "PostFeedback",
    "group": "Feedback",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "studentId",
            "description": "<p>反馈者为学生，则发送该字段</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "teacherId",
            "description": "<p>反馈者为教师，则发送该字段</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "name",
            "description": "<p>反馈者姓名</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phone",
            "description": "<p>反馈者联系电话</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "content",
            "description": "<p>反馈内容</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},         // 创建后的反馈信息\n  msg:  \"操作成功\"\n}",
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
    "filename": "docjs/feedback.js",
    "groupTitle": "反馈"
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
    "type": "get",
    "url": "/signs/scanning/:code",
    "title": "扫描签到二维码",
    "version": "1.0.0",
    "name": "GetSignScanning",
    "group": "Sign",
    "description": "<p>根据签到码code，返回相关课程信息与最多10条最近的学生签到记录</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: \n  { \n    signId: ''          // 签到id\n    course: \n    {\n      name: '',\n      time: '',\n      location: '',\n      teacherName: ''\n    },         \n    records:            // 最多10条最近的学生签到记录\n    [{\n      _id: '',          // 签到记录id\n      name: '',         // 学生姓名\n      avatar: ''        // 学生头像url\n    }]  \n  },\n  msg:  \"操作成功\"\n}",
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
    "url": "/signRecords",
    "title": "获取所有",
    "version": "1.0.0",
    "name": "GetSignRecord",
    "group": "SignRecord",
    "description": "<p>仅供调试。</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],         // 查询到的所有签到记录\n  msg:  \"操作成功\"\n}",
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
    "filename": "docjs/sign-record.js",
    "groupTitle": "签到记录"
  },
  {
    "type": "post",
    "url": "/signRecords",
    "title": "学生进行签到",
    "version": "1.0.0",
    "name": "PostSignRecord",
    "group": "SignRecord",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "signId",
            "description": "<p>所属签到id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phoneId",
            "description": "<p>手机唯一标识</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "studentId",
            "description": "<p>学生id</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "type",
            "description": "<p>签到类型（0：课前，1：课后）</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "battery",
            "description": "<p>手机电量</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "longitude",
            "description": "<p>精度</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "latitude",
            "description": "<p>纬度</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},         // 创建后的签到记录\n  msg:  \"操作成功\"\n}",
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
    "filename": "docjs/sign-record.js",
    "groupTitle": "签到记录"
  },
  {
    "type": "post",
    "url": "/signRecords/search",
    "title": "查询",
    "version": "1.0.0",
    "name": "PostSignRecordSearch",
    "group": "SignRecord",
    "description": "<p>根据指定字段名和值查询签到记录。（必须要有查询条件）</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "_id",
            "description": "<p>签到记录id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "signId",
            "description": "<p>签到id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "phoneId",
            "description": "<p>手机唯一标识</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "studentId",
            "description": "<p>学生id</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "studentName",
            "description": "<p>学生姓名</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "studentAvatar",
            "description": "<p>学生头像url</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "distance",
            "description": "<p>签到距离</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "state",
            "description": "<p>签到批准状态（0：未处理 1：已准许 2：已拒绝）</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "type",
            "description": "<p>签到类型（0：课前，1：课后）</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "createdAt",
            "description": "<p>签到时间</p>"
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "battery",
            "description": "<p>手机电量</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "成功：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: [],           // 查询到的所有签到记录\n  msg:  \"操作成功\"\n}",
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
    "filename": "docjs/sign-record.js",
    "groupTitle": "签到记录"
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
    "url": "/students/:id/activeInfo",
    "title": "发言与签到",
    "version": "1.0.0",
    "name": "GetStudentActiveInfo",
    "group": "Student",
    "description": "<p>获取指定学生的发言数与签到数</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: \n  {\n    msgCount: 0,            // 发言数\n    signCount: 0            // 签到数\n  }\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},         \n  msg:  \"错误信息\"\n}",
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
    "type": "get",
    "url": "/students/:phone/notice?type=0&page=0&limit=10",
    "title": "相关通知",
    "version": "1.0.0",
    "name": "GetStudentNotice",
    "group": "Student",
    "description": "<p>获取学生相关通知信息，phone指学生手机号，type默认为0，表示课前的通知，limit指每页大小（默认10），page指第几页（默认0）</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data:                                 // 对象数组\n  [{\n    courseName: 'XXX',                  // 课程名称\n    signState: 0,                       // 批准状态\n    signDistance: 0,                    // 签到距离\n    signNumber: 0,                      // 签到完成人数\n    signAt: '2016-09-01 11:00:00'       // 签到时间\n    confirmAt: '2016-09-01 11:00:20'    // 教师确认签到时间\n  }]\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],         \n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/students.js",
    "groupTitle": "学生"
  },
  {
    "type": "get",
    "url": "/students/:phone/relatedCourses?limit=10&page=0&keyword=XX",
    "title": "相关课程的签到信息",
    "version": "1.0.0",
    "name": "GetStudentRelatedCourses",
    "group": "Student",
    "description": "<p>相关课程是指学生参与过该课程的签到，phone指学生手机号，limit指每页大小（默认10），page指第几页（默认0），keyword表示课程名字的搜索关键字（默认空字符串）</p>",
    "success": {
      "examples": [
        {
          "title": "成功",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data:               // 对象数组\n  [{\n    name: 'XXX',      // 课程名称\n    number: 0,        // 签到完成人数\n    courseId: 'XXX'   // 签到所属课程id\n    avatars: []       // 最后完成签到的最多6位学生的头像\n  }]\n  msg:  \"操作成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: [],         \n  msg:  \"错误信息\"\n}",
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
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: { user: {}, person: {} },         // user注册成功的用户的信息,person为注册成功的教师或学生信息\n  msg:  \"请求成功\"\n}",
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
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: { user: {}, person: {} }          // user登录成功的用户的信息，person为登录成功的教师或学生信息\n  msg:  \"请求成功\"\n}",
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
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: { user: {}, person: {} }          // user登录成功的用户的信息，person为登录成功的教师或学生信息\n  msg:  \"请求成功\"\n}",
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
  },
  {
    "type": "put",
    "url": "/users/:id",
    "title": "修改密码",
    "version": "1.0.0",
    "name": "PutUser",
    "group": "User",
    "description": "<p>修改指定id用户的密码。</p>",
    "parameter": {
      "fields": {
        "Parameter": [
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
          "content": "HTTP/1.1 200 OK\n{\n  code: \"200\",\n  data: {},         // 修改密码的用户信息，不包含密码\n  msg:  \"请求成功\"\n}",
          "type": "json"
        }
      ]
    },
    "error": {
      "examples": [
        {
          "title": "失败：",
          "content": "HTTP/1.1 200 OK\n{\n  code: \"XXXX\",\n  data: {},         \n  msg:  \"错误信息\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "docjs/users.js",
    "groupTitle": "用户"
  }
] });
