import smtplib, ssl
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

from jinja2 import Environment, FileSystemLoader


class Client:
    def __init__(
            self,
            user: str,
            password: str,
            hostname: str,
            port: int,
            path_to_templates: str,
            message_template: str
    ):
        self.__user = user
        self.__password = password
        self.__smtp_host = hostname
        self.__smtp_port = port
        self.__path_to_templates = path_to_templates
        self.__template_name = message_template

    def send_authentication_code(self, receptionist, code: str):
        msg = MIMEMultipart()
        msg['From'] = "Monidorm authentication service"
        msg['To'] = receptionist
        msg['Subject'] = "Monidorm bot authentication code"

        body = self.render_template(code)
        msg.attach(MIMEText(body, 'html'))

        context = ssl.create_default_context()
        with smtplib.SMTP_SSL(self.__smtp_host, self.__smtp_port, context=context) as server:
            server.login(self.__user, self.__password)
            server.sendmail(self.__user, receptionist, msg.as_string())

    def render_template(self, code: str):
        env = Environment(loader=FileSystemLoader(self.__path_to_templates))
        template = env.get_template(self.__template_name)
        return template.render({
            "code": code
        })
