export default {
  "cn": "设备维保",
  "en": "MAINTENANCE OS",
  "subtitle": "设备与维保工单",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "assets",
      "title": "设备档案",
      "singular": "设备档案",
      "icon": "▣",
      "fields": [
        [
          "name",
          "设备档案名称",
          "text",
          1
        ],
        [
          "model",
          "设备型号",
          "text",
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
        "model",
        "location"
      ]
    },
    {
      "key": "workorders",
      "title": "维保工单",
      "singular": "维保工单",
      "icon": "◇",
      "fields": [
        [
          "assetId",
          "关联设备档案",
          "relation",
          1,
          "assets"
        ],
        [
          "technician",
          "维保人员",
          "text",
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
            "待处理",
            "处理中",
            "已完成"
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
        "assetId",
        "technician",
        "eventDate",
        "status"
      ]
    }
  ]
}
