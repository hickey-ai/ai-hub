export default {
  "cn": "冷链运输",
  "en": "COLDCHAIN OS",
  "subtitle": "容器与运输登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "containers",
      "title": "冷链箱档案",
      "singular": "冷链箱档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "冷链箱档案名称",
          "text",
          1
        ],
        [
          "targetTemp",
          "目标温度（℃）",
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
        "targetTemp",
        "location"
      ]
    },
    {
      "key": "transports",
      "title": "运输记录",
      "singular": "运输记录",
      "icon": "◇",
      "fields": [
        [
          "containerId",
          "关联冷链箱档案",
          "relation",
          1,
          "containers"
        ],
        [
          "route",
          "运输线路",
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
            "待发运",
            "在途",
            "已到达"
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
        "containerId",
        "route",
        "eventDate",
        "status"
      ]
    }
  ]
}
