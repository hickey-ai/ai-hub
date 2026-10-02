export default {
  "cn": "健身房",
  "en": "GYM OS",
  "subtitle": "课程与预约台账",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "classes",
      "title": "团体课程",
      "singular": "团体课程",
      "icon": "▣",
      "fields": [
        [
          "name",
          "团体课程名称",
          "text",
          1
        ],
        [
          "capacity",
          "课程人数",
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
      "key": "bookings",
      "title": "课程预约",
      "singular": "课程预约",
      "icon": "◇",
      "fields": [
        [
          "classeId",
          "关联团体课程",
          "relation",
          1,
          "classes"
        ],
        [
          "memberAlias",
          "会员代称",
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
            "待上课",
            "已签到",
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
        "memberAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
