export default {
  "cn": "港口航运记录",
  "en": "PORT FLOW",
  "subtitle": "泊位与靠港记录台账",
  "accent": "#236f8d",
  "soft": "#eaf5fa",
  "entities": [
    {
      "key": "berths",
      "title": "港口泊位",
      "singular": "港口泊位",
      "icon": "▣",
      "fields": [
        [
          "code",
          "泊位编号",
          "text",
          1
        ],
        [
          "name",
          "泊位名称",
          "text",
          1
        ],
        [
          "terminal",
          "所属码头",
          "text",
          1
        ],
        [
          "capacity",
          "参考吨位",
          "number",
          1
        ]
      ],
      "columns": [
        "code",
        "name",
        "terminal",
        "capacity"
      ]
    },
    {
      "key": "portcalls",
      "title": "靠港记录",
      "singular": "靠港记录",
      "icon": "↗",
      "fields": [
        [
          "berthId",
          "关联泊位",
          "relation",
          1,
          "berths"
        ],
        [
          "vessel",
          "船舶名称",
          "text",
          1
        ],
        [
          "eventDate",
          "靠港日期",
          "date",
          1
        ],
        [
          "cargo",
          "货物摘要",
          "text",
          1
        ],
        [
          "status",
          "登记状态",
          "select",
          1,
          [
            "计划中",
            "靠泊中",
            "已离港"
          ]
        ],
        [
          "notes",
          "调度备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "berthId",
        "vessel",
        "eventDate",
        "cargo",
        "status"
      ]
    }
  ]
}
