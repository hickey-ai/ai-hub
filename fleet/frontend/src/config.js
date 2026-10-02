export default {
  "cn": "车队管理",
  "en": "FLEET OS",
  "subtitle": "车队与行车任务",
  "accent": "#217c9a",
  "soft": "#e8f5f9",
  "entities": [
    {
      "key": "vehicles",
      "title": "车队车辆",
      "singular": "车队车辆",
      "icon": "▣",
      "fields": [
        [
          "name",
          "车队车辆名称",
          "text",
          1
        ],
        [
          "plate",
          "车辆编号",
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
        "plate",
        "location"
      ]
    },
    {
      "key": "trips",
      "title": "行车任务",
      "singular": "行车任务",
      "icon": "◇",
      "fields": [
        [
          "vehicleId",
          "关联车队车辆",
          "relation",
          1,
          "vehicles"
        ],
        [
          "driver",
          "驾驶人员",
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
            "待发车",
            "运输中",
            "已归档"
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
        "vehicleId",
        "driver",
        "eventDate",
        "status"
      ]
    }
  ]
}
