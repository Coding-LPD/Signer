/**
 * @api {get} /courses 获取所有
 * @apiVersion 1.0.0
 * @apiName GetCourse
 * @apiGroup Course
 * @apiDescription 仅供调试。
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],           // 所有课程
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],           // 空数组
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /courses 创建
 * @apiVersion 1.0.0
 * @apiName PostCourse
 * @apiGroup Course
 * 
 * @apiParam {String} name          课程名称
 * @apiParam {String} time          上课时间（格式：星期一 1节-4节,星期二 3节-4节）
 * @apiParam {String} location      上课地点
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},           // 创建后的课程信息
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},           // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {delete} /courses/:id 删除
 * @apiVersion 1.0.0
 * @apiName DeleteCourse
 * @apiGroup Course
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},         // 删除的课程的所有信息
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},           // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /courses/search 查询
 * @apiVersion 1.0.0
 * @apiName PostCourseSearch
 * @apiGroup Course
 * @apiDescription 根据指定字段名和值查询课程。（必须要有查询条件）
 * 
 * @apiParam {String} _id             课程id
 * @apiParam {String} name            课程名称
 * @apiParam {String} teacherId       所属教师id
 * @apiParam {String} location        上课地点
 * @apiParam {String} academy         学院（excel导入）
 * @apiParam {String} time            上课时间
 * @apiParam {Number} studentCount    学生数量
 * @apiParam {Number} signCount       签到次数
 * @apiParam {Number} state           课程状态（0：未开始，1：进行中，2：已结束）
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],           // 查询到的所有课程
 *      msg:  "操作成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],           // 空数组
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {get} /courses/:id/latestSignRecords 最近签到记录
 * @apiVersion 1.0.0
 * @apiName GetCourseLatestSignRecord
 * @apiGroup Course
 * @apiDescription 查询指定id的课程的最近签到记录，返回课程信息和最多20个最近签到者头像
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: 
 *      {
 *        signNum: XXX        // 签到次数
 *        course: 
 *        {
 *          name: '',
 *          time: '',
 *          location: '',
 *          teacherName: ''
 *        }          
 *        records:            // 最多20个最近签到记录
 *        [{
 *          _id: 'XXX',       // 签到记录id
 *          name: '',         // 学生姓名
 *          avatar: ''        // 学生头像url
 *        }]         
 *      },          
 *      msg:  "操作成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},           // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {get} /courses/:id/students/:studentId/signRecords 指定学生的签到情况
 * @apiVersion 1.0.0
 * @apiName GetCourseStudentSignRecord
 * @apiGroup Course
 * @apiDescription 查询指定studentId的学生在指定id的课程中，所有的签到情况
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: 
 *      [{
 *        signId: '',       // 签到id
 *        time: '',         // 签到日期（2016-09-10）
 *        tag: ''           // 签到完成情况（true完成，false没完成）
 *      }]          
 *      msg:  "操作成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],           // 空数组
 *      msg:  "错误信息"
 *    }
 */