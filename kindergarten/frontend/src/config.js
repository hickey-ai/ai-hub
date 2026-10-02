export default {
  "cn": "幼儿园管理",
  "en": "KINDERGARTEN OS",
  "subtitle": "班级与活动记录",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "classes",
      "title": "班级档案",
      "singular": "班级档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "班级档案名称",
          "text",
          1
        ],
        [
          "capacity",
          "班级容量",
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
        "capacity",
        "location"
      ]
    },
    {
      "key": "activities",
      "title": "班级活动",
      "singular": "班级活动",
      "icon": "◇",
      "fields": [
        [
          "classeId",
          "关联班级档案",
          "relation",
          1,
          "classes"
        ],
        [
          "teacher",
          "带班教师",
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
            "筹备中",
            "进行中",
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
        "classeId",
        "teacher",
        "eventDate",
        "status"
      ]
    }
  ]
}
