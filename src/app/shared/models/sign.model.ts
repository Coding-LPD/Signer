export class Sign {

  constructor(
    public _id?: string,
    public courseId?: string,
    public courseName?: string,
    public teacherId?: string,
    public startTime?: string,
    public endTime?: string,
    public createdAt?: string,
    public state?: number,
    public beforeSignIn?: number,
    public afterSignIn?: number,
    public color?: string,
    public relatedId?: string,
    public code?: string,
    public studentCount?: number,
    public isAfterOpen?: boolean
  ) {}

}