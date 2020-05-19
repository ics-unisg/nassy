import subprocess
import sys

subprocess.call([sys.executable, "-m", "pip", "install", 'pandas'])
subprocess.call([sys.executable, "-m", "pip", "install", 'numpy'])
subprocess.call([sys.executable, "-m", "pip", "install", 'scipy'])
subprocess.call([sys.executable, "-m", "pip", "install", 'statistics'])
subprocess.call([sys.executable, "-m", "pip", "install", 'mysql-connector-python'])
subprocess.call([sys.executable, "-m", "pip", "install", 'termcolor'])
subprocess.call([sys.executable, "-m", "pip", "install", 'requests'])
subprocess.call([sys.executable, "-m", "pip", "install", 'datetime'])
