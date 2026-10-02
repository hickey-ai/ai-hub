export default {
  "cn": "生鲜配送",
  "en": "FRESHDELIVERY OS",
  "subtitle": "线路与配送任务",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "routes",
      "title": "配送线路",
      "singular": "配送线路",
      "icon": "▣",
      "fields": [
        [
          "name",
          "配送线路名称",
          "text",
          1
        ],
        [
          "region",
          "配送区域",
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
        "region",
        "location"
      ]
    },
    {
      "key": "deliveries",
      "title": "配送任务",
      "singular": "配送任务",
      "icon": "◇",
      "fields": [
        [
          "routeId",
          "关联配送线路",
          "relation",
          1,
          "routes"
        ],
        [
          "driver",
          "配送人员",
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
            "待分配",
            "配送中",
            "已送达"
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
        "routeId",
        "driver",
        "eventDate",
        "status"
      ]
    }
  ]
}
