/**
 * @api {get} /students 获取所有
 * @apiVersion 1.0.0
 * @apiName GetStudent
 * @apiGroup Student
 * @apiDescription 仅供调试。
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],         // 查询到的所有学生
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],         // 空数组
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {put} /students/:id 修改
 * @apiVersion 1.0.0
 * @apiName PutStudent
 * @apiGroup Student
 * @apiDescription 根据指定字段名和值修改指定id的学生
 * 
 * @apiSuccess {String}  code  200表示成功。
 * @apiSuccess {Object}   data  修改后的学生信息
 * 
 * @apiParam {String} number    学号（可选）
 * @apiParam {String} name      姓名（可选）
 * @apiParam {String} gender    性别（可选）
 * @apiParam {String} school    学校（可选）
 * @apiParam {String} academy   学院（可选）
 * @apiParam {String} major     专业（可选）
 * @apiParam {String} grade     年级（可选）
 * @apiParam {String} class     班级（可选）
 * @apiParam {String} mail      邮箱（可选）
 * @apiParam {String} avatar    头像url（可选）
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},         // 修改后的学生信息
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},         // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /students/search 查询
 * @apiVersion 1.0.0
 * @apiName PostStudentSearch
 * @apiGroup Student
 * @apiDescription 根据指定字段名和值查询学生。（必须要有查询条件）
 * 
 * @apiParam {String} _id       学生id（可选）
 * @apiParam {String} phone     手机号码（可选）
 * @apiParam {String} number    学号（可选）
 * @apiParam {String} name      姓名（可选）
 * @apiParam {String} gender    性别（可选）
 * @apiParam {String} school    学校（可选）
 * @apiParam {String} academy   学院（可选）
 * @apiParam {String} major     专业（可选）
 * @apiParam {String} grade     年级（可选）
 * @apiParam {String} class     班级（可选）
 * @apiParam {String} mail      邮箱（可选）
 * @apiParam {String} avatar    头像url（可选）
 * 
 * @apiSuccess {String}  code  200表示成功。
 * @apiSuccess {Array}   data  符合条件的所有学生
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],         // 查询到的所有学生
 *      msg:  "操作成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],         // 空数组
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /students/images 上传头像
 * @apiVersion 1.0.0
 * @apiName PostStudentImage
 * @apiGroup Student
 * 
 * @apiHeaderExample {json} 请求头部
 * {
 *    "Content-Type": "multipart/form-data"
 * }
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: "http://linkdust.xicp.net:50843/images/XXX.png",      // 头像url
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: "",         
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {get} /students/images/:id 获取默认头像url
 * @apiVersion 1.0.0
 * @apiName GetStudentImage
 * @apiGroup Student
 * @apiDescription id可取值为1、2、3、4，代表4张不同的默认图片 
 * 
 * @apiSuccess {String}  code  200表示成功。
 * @apiSuccess {String}  data  头像url 
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: "http://linkdust.xicp.net:50843/images/user/1.png",    // 头像url
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: "",         
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {get} /students/:phone/relatedCourses?limit=10&page=0 相关课程的签到信息
 * @apiVersion 1.0.0
 * @apiName GetStudentRelatedCourses
 * @apiGroup Student
 * @apiDescription 相关课程是指学生参与过该课程的签到，phone指学生手机号，limit指每页大小（默认10），page指第几页（默认0）
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data:               // 对象数组
 *      [{
 *        name: 'XXX',      // 课程名称
 *        number: 0,        // 签到完成人数
 *        courseId: 'XXX'   // 签到所属课程id
 *        avatars: []       // 最后完成签到的最多6位学生的头像
 *      }]
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],         
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {get} /students/:phone/notice?type=0&page=0&limit=10 相关通知
 * @apiVersion 1.0.0
 * @apiName GetStudentNotice
 * @apiGroup Student
 * @apiDescription 获取学生相关通知信息，phone指学生手机号，type默认为0，表示课前的通知，limit指每页大小（默认10），page指第几页（默认0）
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data:                                 // 对象数组
 *      [{
 *        courseName: 'XXX',                  // 课程名称
 *        signState: 0,                       // 批准状态
 *        signDistance: 0,                    // 签到距离
 *        signNumber: 0,                      // 签到完成人数
 *        signAt: '2016-09-01 11:00:00'       // 签到时间
 *        confirmAt: '2016-09-01 11:00:20'    // 教师确认签到时间
 *      }]
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],         
 *      msg:  "错误信息"
 *    }
 */