export default {
  "cn": "水务设施",
  "en": "WATER WORKS",
  "subtitle": "水务设施与巡检数据的本机记录台",
  "accent": "#14859e",
  "soft": "#e7f6fa",
  "entities": [
    {
      "key": "stations",
      "title": "设施档案",
      "singular": "设施档案",
      "icon": "▦",
      "fields": [
        [
          "code",
          "设施编号",
          "text",
          1
        ],
        [
          "name",
          "设施名称",
          "text",
          1
        ],
        [
          "district",
          "区域",
          "text",
          1
        ],
        [
          "status",
          "设施状态",
          "select",
          1,
          [
            "正常",
            "检修",
            "停用"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "district",
        "status"
      ]
    },
    {
      "key": "checks",
      "title": "巡检记录",
      "singular": "巡检记录",
      "icon": "✦",
      "fields": [
        [
          "stationId",
          "关联设施",
          "relation",
          1,
          "stations"
        ],
        [
          "date",
          "巡检日期",
          "date",
          1
        ],
        [
          "inspector",
          "巡检人",
          "text",
          1
        ],
        [
          "reading",
          "读数",
          "number",
          1
        ],
        [
          "result",
          "巡检结论",
          "select",
          1,
          [
            "正常",
            "待复查",
            "已报修"
          ]
        ],
        [
          "notes",
          "巡检备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "stationId",
        "date",
        "inspector",
        "reading",
        "result"
      ]
    }
  ]
}
