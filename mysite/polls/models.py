from django.db import models
import datetime
from django.utils import timezone
# Create your models here.

class Question(models.Model):
    qt=models.CharField(max_length=200)
    pd=models.DateTimeField('date published')

    def __str__(self):
        return self.qt

class Choice(models.Model):
    q=models.ForeignKey(Question, on_delete=models.CASCADE)
    ct=models.CharField(max_length=200)
    votes=models.IntegerField(default=0)
 
    def __str__(self):
        return self.ct
