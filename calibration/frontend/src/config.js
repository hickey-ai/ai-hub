export default {
  "cn": "仪器计量校准",
  "en": "CALIBRATION",
  "subtitle": "设备与校准记录追踪",
  "accent": "#24867d",
  "soft": "#e9f8f4",
  "entities": [
    {
      "key": "instruments",
      "title": "仪器档案",
      "singular": "仪器档案",
      "icon": "▣",
      "fields": [
        [
          "assetNo",
          "资产编号",
          "text",
          1
        ],
        [
          "name",
          "仪器名称",
          "text",
          1
        ],
        [
          "location",
          "使用位置",
          "text",
          1
        ],
        [
          "owner",
          "保管人",
          "text",
          1
        ]
      ],
      "columns": [
        "assetNo",
        "name",
        "location",
        "owner"
      ]
    },
    {
      "key": "calibrations",
      "title": "校准记录",
      "singular": "校准记录",
      "icon": "✓",
      "fields": [
        [
          "instrumentId",
          "关联仪器",
          "relation",
          1,
          "instruments"
        ],
        [
          "certificateNo",
          "记录编号",
          "text",
          1
        ],
        [
          "eventDate",
          "校准日期",
          "date",
          1
        ],
        [
          "dueDate",
          "下次日期",
          "date",
          1
        ],
        [
          "status",
          "记录状态",
          "select",
          1,
          [
            "待复核",
            "已记录",
            "待复检"
          ]
        ],
        [
          "notes",
          "校准说明",
          "textarea",
          0
        ]
      ],
      "columns": [
        "instrumentId",
        "certificateNo",
        "eventDate",
        "dueDate",
        "status"
      ]
    }
  ]
}
