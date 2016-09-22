export class SignRecord {

  constructor(
    public _id?: string,
    public signId?: string,
    public phoneId?: string,
    public studentId?: string,
    public studentName?: string,
    public studentAvatar?: string,    
    public distance?: number,
    public state?: number,
    public type?: number,
    public createdAt?: string,
    public battery?: number      
  ) {}

}