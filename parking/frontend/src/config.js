export default {
  "cn": "停车场",
  "en": "PARKING OS",
  "subtitle": "车位与进出场登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "lots",
      "title": "停车区域",
      "singular": "停车区域",
      "icon": "▣",
      "fields": [
        [
          "name",
          "停车区域名称",
          "text",
          1
        ],
        [
          "capacity",
          "车位总数",
          "text",
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
      "title": "停车记录",
      "singular": "停车记录",
      "icon": "◇",
      "fields": [
        [
          "lotId",
          "关联停车区域",
          "relation",
          1,
          "lots"
        ],
        [
          "plate",
          "车牌编号",
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
            "在场",
            "已离场"
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
        "lotId",
        "plate",
        "eventDate",
        "status"
      ]
    }
  ]
}
