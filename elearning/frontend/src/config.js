export default {
  "cn": "在线学习",
  "en": "ELEARNING OS",
  "subtitle": "课程与学习记录",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "courses",
      "title": "线上课程",
      "singular": "线上课程",
      "icon": "▣",
      "fields": [
        [
          "name",
          "线上课程名称",
          "text",
          1
        ],
        [
          "lessons",
          "章节数量",
          "number",
          1
        ],
        [
          "location",
          "位置／备注",
          "text",
          1
        ]
      ],
      "columns": [
        "name",
        "lessons",
        "location"
      ]
    },
    {
      "key": "progress",
      "title": "学习进度",
      "singular": "学习进度",
      "icon": "◇",
      "fields": [
        [
          "courseId",
          "关联线上课程",
          "relation",
          1,
          "courses"
        ],
        [
          "learnerAlias",
          "学员代称",
          "text",
          1
        ],
        [
          "eventDate",
          "登记日期",
          "date",
          1
        ],
        [
          "status",
          "状态",
          "select",
          1,
          [
            "未开始",
            "学习中",
            "已完成"
          ]
        ],
        [
          "notes",
          "备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "courseId",
        "learnerAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
