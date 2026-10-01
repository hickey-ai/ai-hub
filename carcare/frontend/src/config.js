export default {
  "cn": "汽车维修记录",
  "en": "AUTONOTE",
  "subtitle": "车况、保养、费用，一本账都记清楚",
  "accent": "#176eb5",
  "soft": "#e8f4ff",
  "entities": [
    {
      "key": "vehicles",
      "title": "我的车辆",
      "singular": "车辆",
      "icon": "▣",
      "fields": [
        [
          "plate",
          "车牌号",
          "text",
          true
        ],
        [
          "model",
          "品牌车型",
          "text",
          true
        ],
        [
          "mileage",
          "当前里程（km）",
          "number",
          true
        ],
        [
          "notes",
          "车辆备注",
          "textarea",
          false
        ]
      ],
      "columns": [
        "plate",
        "model",
        "mileage"
      ]
    },
    {
      "key": "services",
      "title": "维修保养",
      "singular": "记录",
      "icon": "⚙",
      "fields": [
        [
          "vehicleId",
          "关联车辆",
          "relation",
          true,
          "vehicles"
        ],
        [
          "date",
          "维修日期",
          "date",
          true
        ],
        [
          "type",
          "服务类型",
          "select",
          true,
          [
            "常规保养",
            "维修",
            "轮胎",
            "保险",
            "其他"
          ]
        ],
        [
          "mileage",
          "当时里程（km）",
          "number",
          true
        ],
        [
          "cost",
          "费用（元）",
          "money",
          true
        ],
        [
          "notes",
          "项目与凭据备注",
          "textarea",
          false
        ]
      ],
      "columns": [
        "vehicleId",
        "date",
        "type",
        "mileage",
        "cost"
      ]
    }
  ]
}
