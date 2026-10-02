export default {
  "cn": "考试管理",
  "en": "EXAM OS",
  "subtitle": "场次与报考登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "exams",
      "title": "考试场次",
      "singular": "考试场次",
      "icon": "▣",
      "fields": [
        [
          "name",
          "考试场次名称",
          "text",
          1
        ],
        [
          "seatCount",
          "座位数量",
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
        "seatCount",
        "location"
      ]
    },
    {
      "key": "registrations",
      "title": "报考登记",
      "singular": "报考登记",
      "icon": "◇",
      "fields": [
        [
          "examId",
          "关联考试场次",
          "relation",
          1,
          "exams"
        ],
        [
          "candidateAlias",
          "考生代称",
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
            "待确认",
            "已确认",
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
        "examId",
        "candidateAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
