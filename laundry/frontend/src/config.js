export default {
  "cn": "洗衣门店",
  "en": "LAUNDRY OS",
  "subtitle": "设备与洗护订单",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "machines",
      "title": "洗护设备",
      "singular": "洗护设备",
      "icon": "▣",
      "fields": [
        [
          "name",
          "洗护设备名称",
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
      "key": "orders",
      "title": "洗护订单",
      "singular": "洗护订单",
      "icon": "◇",
      "fields": [
        [
          "machineId",
          "关联洗护设备",
          "relation",
          1,
          "machines"
        ],
        [
          "garment",
          "衣物描述",
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
            "待收件",
            "洗护中",
            "待取件"
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
        "machineId",
        "garment",
        "eventDate",
        "status"
      ]
    }
  ]
}
