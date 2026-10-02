export default {
  "cn": "林地巡护",
  "en": "FOREST WATCH",
  "subtitle": "林区档案与巡护记录，让观察和处理都有迹可循",
  "accent": "#508461",
  "soft": "#e9f5ec",
  "entities": [
    {
      "key": "parcels",
      "title": "林区档案",
      "singular": "林区档案",
      "icon": "▦",
      "fields": [
        [
          "code",
          "林区编号",
          "text",
          1
        ],
        [
          "name",
          "林区名称",
          "text",
          1
        ],
        [
          "area",
          "面积（亩）",
          "money",
          1
        ],
        [
          "manager",
          "负责人员",
          "text",
          1
        ],
        [
          "status",
          "林区状态",
          "select",
          1,
          [
            "正常",
            "待巡查",
            "暂停开放"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "area",
        "manager",
        "status"
      ]
    },
    {
      "key": "patrols",
      "title": "巡护记录",
      "singular": "巡护记录",
      "icon": "✦",
      "fields": [
        [
          "parcelId",
          "关联林区",
          "relation",
          1,
          "parcels"
        ],
        [
          "date",
          "巡护日期",
          "date",
          1
        ],
        [
          "observer",
          "巡护人员",
          "text",
          1
        ],
        [
          "finding",
          "观察结论",
          "select",
          1,
          [
            "无异常",
            "待复查",
            "已上报"
          ]
        ],
        [
          "notes",
          "巡护备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "parcelId",
        "date",
        "observer",
        "finding"
      ]
    }
  ]
}
