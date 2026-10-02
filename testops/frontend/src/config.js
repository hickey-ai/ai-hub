export default {
  "cn": "测试管理平台",
  "en": "TEST STUDIO",
  "subtitle": "从测试用例到手工执行结果，保留可追溯的测试记录",
  "accent": "#5359d6",
  "soft": "#eff0ff",
  "entities": [
    {
      "key": "testcases",
      "title": "测试用例",
      "singular": "测试用例",
      "icon": "▤",
      "fields": [
        [
          "caseNo",
          "用例编号",
          "text",
          1
        ],
        [
          "title",
          "用例标题",
          "text",
          1
        ],
        [
          "module",
          "所属模块",
          "text",
          1
        ],
        [
          "priority",
          "优先级",
          "select",
          1,
          [
            "P0",
            "P1",
            "P2",
            "P3"
          ]
        ],
        [
          "steps",
          "操作步骤",
          "textarea",
          1
        ],
        [
          "expected",
          "预期结果",
          "textarea",
          1
        ]
      ],
      "columns": [
        "caseNo",
        "title",
        "module",
        "priority"
      ]
    },
    {
      "key": "executions",
      "title": "执行记录",
      "singular": "执行记录",
      "icon": "✓",
      "fields": [
        [
          "caseId",
          "关联用例",
          "relation",
          1,
          "testcases"
        ],
        [
          "version",
          "测试版本",
          "text",
          1
        ],
        [
          "eventDate",
          "执行日期",
          "date",
          1
        ],
        [
          "result",
          "执行结果",
          "select",
          1,
          [
            "未执行",
            "通过",
            "失败",
            "阻塞"
          ]
        ],
        [
          "status",
          "复核状态",
          "select",
          1,
          [
            "待复核",
            "已复核"
          ]
        ],
        [
          "tester",
          "执行人",
          "text",
          1
        ],
        [
          "actual",
          "实际结果",
          "textarea",
          0
        ]
      ],
      "columns": [
        "caseId",
        "version",
        "eventDate",
        "result",
        "status",
        "tester"
      ]
    }
  ]
}
