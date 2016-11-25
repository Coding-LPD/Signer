export class ChatMsg {

  constructor(
    public _id?: string,
    public courseId?: string,
    public teacherId?: string,
    public studentId?: string,
    public content?: string,
    public avatar?: string,
    public name?: string,
    public createdAt?: string
  ) {}

}