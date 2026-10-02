export default {
  "cn": "IT 运维",
  "en": "OPS CENTER",
  "subtitle": "资产与故障记录，为运维工作提供清晰的时间线",
  "accent": "#555eaa",
  "soft": "#eef0fc",
  "entities": [
    {
      "key": "assets",
      "title": "设备资产",
      "singular": "设备资产",
      "icon": "▦",
      "fields": [
        [
          "code",
          "资产编号",
          "text",
          1
        ],
        [
          "name",
          "资产名称",
          "text",
          1
        ],
        [
          "owner",
          "归属团队",
          "text",
          1
        ],
        [
          "status",
          "资产状态",
          "select",
          1,
          [
            "在线",
            "维护中",
            "停用"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "owner",
        "status"
      ]
    },
    {
      "key": "incidents",
      "title": "故障记录",
      "singular": "故障记录",
      "icon": "✦",
      "fields": [
        [
          "assetId",
          "关联资产",
          "relation",
          1,
          "assets"
        ],
        [
          "ticketNo",
          "事件编号",
          "text",
          1
        ],
        [
          "title",
          "故障标题",
          "text",
          1
        ],
        [
          "reportedDate",
          "发现日期",
          "date",
          1
        ],
        [
          "severity",
          "严重程度",
          "select",
          1,
          [
            "低",
            "中",
            "高"
          ]
        ],
        [
          "status",
          "处理状态",
          "select",
          1,
          [
            "待处理",
            "处理中",
            "已恢复"
          ]
        ],
        [
          "notes",
          "处置备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "assetId",
        "ticketNo",
        "title",
        "reportedDate",
        "severity",
        "status"
      ]
    }
  ]
}
