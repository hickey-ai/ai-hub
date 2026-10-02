export default {
  "cn": "能源环保",
  "en": "ECO MONITOR",
  "subtitle": "设备与巡检指标并排可见，异常线索及时记录",
  "accent": "#167e75",
  "soft": "#e5f7f3",
  "entities": [
    {
      "key": "assets",
      "title": "监测设备",
      "singular": "监测设备",
      "icon": "▦",
      "fields": [
        [
          "code",
          "设备编号",
          "text",
          1
        ],
        [
          "name",
          "设备名称",
          "text",
          1
        ],
        [
          "site",
          "安装位置",
          "text",
          1
        ],
        [
          "type",
          "设备类型",
          "select",
          1,
          [
            "能耗表",
            "水质仪",
            "空气传感器"
          ]
        ],
        [
          "status",
          "运行状态",
          "select",
          1,
          [
            "正常",
            "检修",
            "停用"
          ]
        ]
      ],
      "columns": [
        "code",
        "name",
        "site",
        "type",
        "status"
      ]
    },
    {
      "key": "inspections",
      "title": "巡检记录",
      "singular": "巡检记录",
      "icon": "✦",
      "fields": [
        [
          "assetId",
          "关联设备",
          "relation",
          1,
          "assets"
        ],
        [
          "date",
          "巡检日期",
          "date",
          1
        ],
        [
          "reading",
          "读数",
          "money",
          1
        ],
        [
          "result",
          "巡检结论",
          "select",
          1,
          [
            "正常",
            "待复核",
            "异常"
          ]
        ],
        [
          "inspector",
          "巡检人",
          "text",
          1
        ],
        [
          "notes",
          "巡检备注",
          "textarea",
          0
        ]
      ],
      "columns": [
        "assetId",
        "date",
        "reading",
        "result",
        "inspector"
      ]
    }
  ]
}
