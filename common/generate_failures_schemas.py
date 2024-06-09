from jinja2 import Environment, FileSystemLoader
from pathlib import Path


TEMPLATES_PATH = '.'
TEMPLATE_NAME = 'failures-schemas-template.json.j2'
OUTPUT_PATH = './generated/failures-schemas.json'

env = Environment(loader=FileSystemLoader(TEMPLATES_PATH))
template = env.get_template(TEMPLATE_NAME)
generated_json = template.render()

output_file = Path(OUTPUT_PATH)
output_file.parent.mkdir(exist_ok=True, parents=True)
output_file.write_text(generated_json)