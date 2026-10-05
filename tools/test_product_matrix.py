"""Keep the bilingual product matrix complete and synchronized with the actual projects."""
import re
import sys
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / 'tools'))
from generate_product_matrix import catalog, render  # noqa: E402


class ProductMatrixTests(unittest.TestCase):
    def test_catalog_and_bilingual_matrix_exactly_cover_runnable_projects(self):
        actual = {p.name for p in ROOT.iterdir() if (p / 'backend/pom.xml').is_file()}
        self.assertEqual(len(actual), 101)
        zh = {anchor: {slug for _, slug in entries} for anchor, _, entries in catalog('zh')}
        en = {anchor: {slug for _, slug in entries} for anchor, _, entries in catalog('en')}
        self.assertEqual(zh, en, 'The primary customer category must agree in both languages')
        for language in ('zh', 'en'):
            with self.subTest(language=language):
                sections = catalog(language)
                names = [slug for _, _, entries in sections for _, slug in entries]
                self.assertEqual(len(sections), 12)
                self.assertEqual(len(names), len(set(names)))
                self.assertEqual(set(names), actual)
                matrix = (ROOT / ('docs/product-matrix.md' if language == 'zh' else 'docs/product-matrix.en.md')).read_text(encoding='utf-8')
                self.assertEqual(matrix, render(language), 'Regenerate with python tools/generate_product_matrix.py')
                table = matrix.split('## 缺失赛道' if language == 'zh' else '## Missing directions')[0]
                linked = re.findall(r'\]\(\.\./([a-z0-9]+)/README\.md\)', table)
                self.assertEqual(len(linked), 101)
                self.assertEqual(set(linked), actual)
                self.assertIn('2025', matrix)
                for row in table.splitlines():
                    if row.startswith('| ['):
                        self.assertEqual(row.count('待建设' if language == 'zh' else 'Not delivered'), 3)


if __name__ == '__main__':
    unittest.main()
