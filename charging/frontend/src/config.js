export default {
  "cn": "充电站",
  "en": "CHARGING OS",
  "subtitle": "充电站点与充电登记",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "stations",
      "title": "充电站点",
      "singular": "充电站点",
      "icon": "▣",
      "fields": [
        [
          "name",
          "充电站点名称",
          "text",
          1
        ],
        [
          "piles",
          "充电桩数量",
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
        "piles",
        "location"
      ]
    },
    {
      "key": "sessions",
      "title": "充电记录",
      "singular": "充电记录",
      "icon": "◇",
      "fields": [
        [
          "stationId",
          "关联充电站点",
          "relation",
          1,
          "stations"
        ],
        [
          "energy",
          "电量（kWh）",
          "number",
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
            "进行中",
            "已完成",
            "异常"
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
        "stationId",
        "energy",
        "eventDate",
        "status"
      ]
    }
  ]
}
