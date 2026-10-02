export default {
  "cn": "物流运输",
  "en": "FLEET FLOW",
  "subtitle": "订单、车辆与运单状态在一张工作台上流动",
  "accent": "#0c8194",
  "soft": "#e9f8fa",
  "entities": [
    {
      "key": "vehicles",
      "title": "运输车辆",
      "singular": "运输车辆",
      "icon": "▣",
      "fields": [
        [
          "plate",
          "车辆编号",
          "text",
          1
        ],
        [
          "driver",
          "驾驶员",
          "text",
          1
        ],
        [
          "capacity",
          "载重（kg）",
          "number",
          1
        ],
        [
          "status",
          "车辆状态",
          "select",
          1,
          [
            "待命",
            "运输中",
            "保养中"
          ]
        ]
      ],
      "columns": [
        "plate",
        "driver",
        "capacity",
        "status"
      ]
    },
    {
      "key": "shipments",
      "title": "运输运单",
      "singular": "运输运单",
      "icon": "↗",
      "fields": [
        [
          "vehicleId",
          "承运车辆",
          "relation",
          1,
          "vehicles"
        ],
        [
          "trackingNo",
          "运单编号",
          "text",
          1
        ],
        [
          "origin",
          "始发地",
          "text",
          1
        ],
        [
          "destination",
          "目的地",
          "text",
          1
        ],
        [
          "dispatchDate",
          "发车日期",
          "date",
          1
        ],
        [
          "status",
          "运单状态",
          "select",
          1,
          [
            "待发车",
            "在途",
            "已签收",
            "异常"
          ]
        ],
        [
          "notes",
          "运输备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "vehicleId",
        "trackingNo",
        "origin",
        "destination",
        "dispatchDate",
        "status"
      ]
    }
  ]
}
