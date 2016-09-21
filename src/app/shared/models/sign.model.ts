export class Sign {

  constructor(
    public _id?: string,
    public courseId?: string,
    public teacherId?: string,
    public startTime?: string,
    public endTime?: string,
    public createdAt?: string,
    public state?: number,
    public signIn?: number,
    public color?: string,
    public relatedId?: string,
    public code?: string,
    public studentCount?: number
  ) {}

}