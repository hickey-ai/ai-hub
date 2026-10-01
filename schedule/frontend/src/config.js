export default {
  "cn": "日程记录",
  "en": "DAYFLOW",
  "subtitle": "把每一天安排得从容有序",
  "accent": "#6757d9",
  "soft": "#f0edff",
  "entities": [
    {
      "key": "events",
      "title": "日程清单",
      "singular": "日程",
      "icon": "◷",
      "fields": [
        [
          "title",
          "事项",
          "text",
          true
        ],
        [
          "startAt",
          "开始时间",
          "datetime-local",
          true
        ],
        [
          "endAt",
          "结束时间",
          "datetime-local",
          true
        ],
        [
          "category",
          "分类",
          "select",
          true,
          [
            "工作",
            "家庭",
            "生活",
            "其他"
          ]
        ],
        [
          "notes",
          "备注",
          "textarea",
          false
        ],
        [
          "done",
          "已完成",
          "checkbox",
          false
        ]
      ],
      "columns": [
        "title",
        "startAt",
        "category",
        "done"
      ]
    }
  ]
}
