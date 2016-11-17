var ChatRoom = {
  courseId: {
    type: String,
    required: true
  },
  studentIds: {
    type: Array,
    default: []
  },
  avatar: {
    type: String,
    default: ''
  },
  name: {
    type: String,
    default: ''
  }
};

module.exports = ChatRoom;