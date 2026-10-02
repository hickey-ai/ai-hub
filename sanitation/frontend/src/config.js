export default {
  "cn": "城市环卫",
  "en": "CITY CLEAN",
  "subtitle": "线路和清运记录，掌握每日作业状况",
  "accent": "#40866b",
  "soft": "#e9f7ef",
  "entities": [
    {
      "key": "routes",
      "title": "作业线路",
      "singular": "作业线路",
      "icon": "▦",
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
          "area",
          "服务片区",
          "text",
          1
        ],
        [
          "frequency",
          "作业频次",
          "text",
          1
        ],
        [
          "status",
          "线路状态",
          "select",
          1,
          [
            "启用",
            "暂停"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "area",
        "frequency",
        "status"
      ]
    },
    {
      "key": "collections",
      "title": "清运记录",
      "singular": "清运记录",
      "icon": "✦",
      "fields": [
        [
          "routeId",
          "关联线路",
          "relation",
          1,
          "routes"
        ],
        [
          "date",
          "作业日期",
          "date",
          1
        ],
        [
          "crew",
          "班组",
          "text",
          1
        ],
        [
          "loads",
          "车次数",
          "number",
          1
        ],
        [
          "status",
          "作业状态",
          "select",
          1,
          [
            "待执行",
            "作业中",
            "已完成",
            "异常"
          ]
        ],
        [
          "notes",
          "作业备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "routeId",
        "date",
        "crew",
        "loads",
        "status"
      ]
    }
  ]
}
