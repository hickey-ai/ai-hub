export default {
  "cn": "养老护理",
  "en": "CARE CIRCLE",
  "subtitle": "照护对象与每日服务记录，虚构数据安心演示",
  "accent": "#a16286",
  "soft": "#fbedf5",
  "entities": [
    {
      "key": "residents",
      "title": "长者档案",
      "singular": "长者档案",
      "icon": "▣",
      "fields": [
        [
          "code",
          "档案编号",
          "text",
          1
        ],
        [
          "name",
          "称呼",
          "text",
          1
        ],
        [
          "careLevel",
          "照护等级",
          "select",
          1,
          [
            "自理",
            "协助",
            "重点关注"
          ]
        ],
        [
          "room",
          "房间编号",
          "text",
          1
        ],
        [
          "status",
          "入住状态",
          "select",
          1,
          [
            "在住",
            "离住"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "careLevel",
        "room",
        "status"
      ]
    },
    {
      "key": "visits",
      "title": "照护记录",
      "singular": "照护记录",
      "icon": "✦",
      "fields": [
        [
          "residentId",
          "关联长者",
          "relation",
          1,
          "residents"
        ],
        [
          "date",
          "服务日期",
          "date",
          1
        ],
        [
          "type",
          "服务类别",
          "select",
          1,
          [
            "日常关怀",
            "饮食协助",
            "活动陪伴",
            "异常报告"
          ]
        ],
        [
          "staff",
          "服务人员",
          "text",
          1
        ],
        [
          "status",
          "记录状态",
          "select",
          1,
          [
            "待执行",
            "已记录"
          ]
        ],
        [
          "notes",
          "服务备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "residentId",
        "date",
        "type",
        "staff",
        "status"
      ]
    }
  ]
}
