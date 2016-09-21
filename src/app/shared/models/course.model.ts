export class Course {

  constructor(
    public _id?: string,
    public name?: string,
    public teacherId?: string,
    public location?: string,
    public time?: string,
    public academy?: string,
    public startTime?: string,
    public endTime?: string,
    public studentCount?: number,
    public signCount?: number,
    public state?: string
  ) {}

}