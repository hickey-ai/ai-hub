export default {
  "cn": "物业房产",
  "en": "PROPERTY ONE",
  "subtitle": "楼宇、房源与服务请求，形成可追溯的管理台账",
  "accent": "#247ca3",
  "soft": "#e9f4fb",
  "entities": [
    {
      "key": "units",
      "title": "房源档案",
      "singular": "房源档案",
      "icon": "▦",
      "fields": [
        [
          "code",
          "房源编号",
          "text",
          1
        ],
        [
          "building",
          "楼栋",
          "text",
          1
        ],
        [
          "floor",
          "楼层",
          "number",
          1
        ],
        [
          "area",
          "面积（㎡）",
          "money",
          1
        ],
        [
          "occupant",
          "住户／租户",
          "text",
          1
        ]
      ],
      "columns": [
        "code",
        "building",
        "floor",
        "area",
        "occupant"
      ]
    },
    {
      "key": "requests",
      "title": "报修工单",
      "singular": "报修工单",
      "icon": "✦",
      "fields": [
        [
          "unitId",
          "关联房源",
          "relation",
          1,
          "units"
        ],
        [
          "title",
          "报修事项",
          "text",
          1
        ],
        [
          "reportedAt",
          "报修日期",
          "date",
          1
        ],
        [
          "priority",
          "优先级",
          "select",
          1,
          [
            "一般",
            "紧急"
          ]
        ],
        [
          "status",
          "处理状态",
          "select",
          1,
          [
            "待受理",
            "处理中",
            "已完成"
          ]
        ],
        [
          "notes",
          "处理备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "unitId",
        "title",
        "reportedAt",
        "priority",
        "status"
      ]
    }
  ]
}
