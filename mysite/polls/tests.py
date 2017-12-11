from django.test import TestCase
from .models import Question, Choice

# Create your tests here.

class TestClass(TestCase):
    def testplus(self):
        q=self.Question.get('qt').verbose_name
        self.assertEqual(q,'qt')        
