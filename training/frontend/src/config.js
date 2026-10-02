export default {
  "cn": "培训机构",
  "en": "TRAINING OS",
  "subtitle": "课程与报名记录",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "courses",
      "title": "培训课程",
      "singular": "培训课程",
      "icon": "▣",
      "fields": [
        [
          "name",
          "培训课程名称",
          "text",
          1
        ],
        [
          "hours",
          "课程课时",
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
        "hours",
        "location"
      ]
    },
    {
      "key": "enrollments",
      "title": "报名登记",
      "singular": "报名登记",
      "icon": "◇",
      "fields": [
        [
          "courseId",
          "关联培训课程",
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
            "已咨询",
            "已登记",
            "已结束"
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
