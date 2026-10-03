export default {
  "cn": "公交地铁运营",
  "en": "TRANSIT DESK",
  "subtitle": "线路与班次登记",
  "accent": "#2564a8",
  "soft": "#eaf3fc",
  "entities": [
    {
      "key": "routes",
      "title": "运营线路",
      "singular": "运营线路",
      "icon": "▤",
      "fields": [
        [
          "code",
          "线路编号",
          "text",
          1
        ],
        [
          "name",
          "线路名称",
          "text",
          1
        ],
        [
          "origin",
          "起点",
          "text",
          1
        ],
        [
          "destination",
          "终点",
          "text",
          1
        ],
        [
          "mode",
          "交通方式",
          "select",
          1,
          [
            "公交",
            "地铁"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "origin",
        "destination",
        "mode"
      ]
    },
    {
      "key": "trips",
      "title": "班次记录",
      "singular": "班次记录",
      "icon": "↗",
      "fields": [
        [
          "routeId",
          "关联线路",
          "relation",
          1,
          "routes"
        ],
        [
          "serviceNo",
          "班次编号",
          "text",
          1
        ],
        [
          "eventDate",
          "运行日期",
          "date",
          1
        ],
        [
          "vehicle",
          "车辆编号",
          "text",
          1
        ],
        [
          "status",
          "班次状态",
          "select",
          1,
          [
            "计划中",
            "运行中",
            "已完成",
            "已取消"
          ]
        ],
        [
          "notes",
          "交接备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "routeId",
        "serviceNo",
        "eventDate",
        "vehicle",
        "status"
      ]
    }
  ]
}
