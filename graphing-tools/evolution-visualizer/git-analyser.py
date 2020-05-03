from pydriller import RepositoryMining, ModificationType
from datetime import datetime, timedelta
from calendar import monthrange

import matplotlib.pyplot as plt
import numpy as np
import os

#Todo: replace with CLI input
REPO_DIR = "/home/simon/development/kotlin/ktor/"
#DIRECTORY_FILTER = "ktor-server/ktor-server-core/"
#Date:   Tue Sep 16 02:45:43 2014 +0400
FIRST_COMMIT = datetime(2014, 9, 16, 0, 0, 0)

def script_foler():
    return os.path.dirname(os.path.realpath(__file__))

def last_day_in_month(date):
    return datetime(date.year, date.month, monthrange(date.year, date.month)[1])

def first_day_in_month(date):
    return datetime(date.year, date.month, 1)

def to_top_level(path):
    return str(path).split("/")[0]

def prepare_modules_dict():
    return {
        "ktor-core": 0,
        "ktor-jetty": 0,
        "ktor-servlet": 0,
        "ktor-components": 0,
        "ktor-samples": 0,
        "ktor-netty": 0,
        "ktor-features": 0,
        "ktor-hosts": 0,
        "ktor-core-tests": 0,
        "ktor-jmh": 0,
        "ktor-server": 0,
        "ktor-client": 0,
        "ktor-http-cio": 0,
        "ktor-network": 0,
        "ktor-utils": 0,
        "ktor-http": 0,
        "ktor-network-tls": 0,
        "ktor-io": 0,
    }

def files_for_month(since, to):
    commit_counts = {}

    for commit in RepositoryMining(REPO_DIR, only_in_branch = 'master', only_modifications_with_file_types = ['.kt'], since=since, to=to).traverse_commits():

        for modification in commit.modifications:
            
            new_path = modification.new_path
            old_path = modification.old_path

            
            try:
                activity = modification.added + modification.removed

                if modification.change_type == ModificationType.RENAME:
                    commit_counts[new_path]=commit_counts.get(old_path,0)+activity
                    commit_counts.pop(old_path)
                    
                    new_module = to_top_level(new_path)
                    old_module = to_top_level(old_path)
                    if(new_module != old_module):
                        #print(commit.hash, "Moving ", old_path," from ", old_module, " to ", new_module)
                        pass

                elif modification.change_type == ModificationType.DELETE:
                    commit_counts.pop(old_path, '')

                elif modification.change_type == ModificationType.ADD:
                    commit_counts[new_path] = activity

                else: # modification to existing file
                        commit_counts [old_path] += activity
            except Exception as e: 
                pass

    return dict(sorted(commit_counts.items(), key=lambda x:x[1], reverse=True))

def file_activity_to_top_modules(top_files):
    modules_activity = prepare_modules_dict()
    for file, activity in top_files.items():
        module = to_top_level(file)
        if module in modules_activity:
            modules_activity[module] += activity
        else:
            #print("Unsupported module:", module, ", file: ", file)
            pass
    return modules_activity

def draw_scatter_for_module(module, data, ax):
    dates = list(data.keys())
    activity_score = list(data.values())
    ax.plot(dates,activity_score, linestyle='-', marker='o', label=module)

def draw_graph(modules_activity_over_time):
    fig = plt.figure(figsize=(20, 10))
    for module, data in modules_activity_over_time.items():
        fig.clf()
        #plt.xticks(rotation=90)
        #plt.legend(loc='upper right')
        ax = fig.add_subplot(111)
        ax.set_title(module)
        ax.tick_params(axis='x', labelrotation=90)
        ax.set_facecolor('#3d3d3d')
        draw_scatter_for_module(module, data, ax)
        plt.tight_layout()
        plt.savefig(f"{script_foler()}/data/{module}_activiy_change_over_time.png")





















start = first_day_in_month(FIRST_COMMIT)
now = datetime.today()

#start = first_day_in_month(datetime(2019, 10, 1, 0, 0, 0))
#now = start + timedelta(days=32)

modules_activity_over_time = {}

while start < now:
    end = last_day_in_month(start)
    files = files_for_month(start, end)

    modules_activity = file_activity_to_top_modules(files)

    for module, activity in modules_activity.items():
        if(module not in modules_activity_over_time):
            modules_activity_over_time[module] = {}

        modules_activity_over_time[module][start.strftime('%B %Y')] = activity
    
    start = start + timedelta(days=end.day)


draw_graph(modules_activity_over_time)