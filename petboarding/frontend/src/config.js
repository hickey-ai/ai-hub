export default {
  "cn": "宠物寄养",
  "en": "PETBOARDING OS",
  "subtitle": "房间与寄养登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "rooms",
      "title": "寄养空间",
      "singular": "寄养空间",
      "icon": "▣",
      "fields": [
        [
          "name",
          "寄养空间名称",
          "text",
          1
        ],
        [
          "capacity",
          "容纳数量",
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
      "key": "stays",
      "title": "寄养登记",
      "singular": "寄养登记",
      "icon": "◇",
      "fields": [
        [
          "roomId",
          "关联寄养空间",
          "relation",
          1,
          "rooms"
        ],
        [
          "petAlias",
          "宠物昵称",
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
            "已预约",
            "寄养中",
            "已离店"
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
        "roomId",
        "petAlias",
        "eventDate",
        "status"
      ]
    }
  ]
}
