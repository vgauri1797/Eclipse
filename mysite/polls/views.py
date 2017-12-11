from django.http import HttpResponse

# Create your views here.

def index(request):
    return HttpResponse("Hello, world. You're at the polls index.")

def detail(request, q_id):
    return HttpResponse("Question: %s" %q_id)

def result(request, q_id):
    return HttpResponse("Result of Question %s" %q_id)

def votes(request, q_id):
    return HttpResponse("Votes of Question %s" %q_id)
