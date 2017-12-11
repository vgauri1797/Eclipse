from django.shortcuts import render_to_response
from django.template import Template, Context
from django.http import HttpResponse
# Create your views here.

def index(request):
    return HttpResponse("Hello")
