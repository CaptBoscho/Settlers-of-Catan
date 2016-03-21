
import unittest
import requests
import subprocess
import time
import json
import argparse


'''
    Integration tests with Python and the Requests library

    http://docs.python-requests.org/en/master/
'''


SERVER_KILL_COMMAND = "lsof -i tcp:8081 | awk 'NR!=1 {print $2}' | xargs kill"
BASE_URL = 'http://localhost:8081/'


class UserTests(unittest.TestCase):

    def test_basic_registration_and_login(self):
        ''' Test basic registration with straight-forward credentials '''
        test_username = 'dummy'
        test_password = 'dummy'
        expected_cookie = '''%7B%22name%22%3A%22dummy%22%2C%22password%22%3A%22dummy%22%2C%22playerID%22%3A0%7D'''
        payload = {
            'username': test_username,
            'password': test_password
        }
        # verify that the login credentials don't exist
        r = requests.post('%suser/login' % BASE_URL, data=json.dumps(payload))
        self.assertEqual(requests.codes.unauthorized, r.status_code)
        self.assertEqual('Failed', r.text)
        self.assertTrue('catan.user' not in r.cookies)

        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(payload))
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual('Success', r.text)
        self.assertEqual(expected_cookie, r.cookies['catan.user'])

        # should now be able to login with these credentials
        r = requests.post('%suser/login' % BASE_URL, data=json.dumps(payload))
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual('Success', r.text)
        self.assertEqual(expected_cookie, r.cookies['catan.user'])

    def test_bad_json(self):
        ''' Validate what happens if you send incorrect JSON '''
        bad_payload = {
            'yolo': 'Taylor Swift'
        }
        r = requests.post('%suser/login' % BASE_URL, data=json.dumps(bad_payload))
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)

        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(bad_payload))
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)


if __name__ == '__main__':

    # kill the server in-case it was left running in IntelliJ or something
    subprocess.call(SERVER_KILL_COMMAND, shell=True)

    # Configurable options
    parser = argparse.ArgumentParser()
    parser.add_argument('--time', type=int, dest='build_time', default=6)
    parser.add_argument('--verbose', type=bool, dest='verbose', default=False)
    args = parser.parse_args()

    try:
        # build server
        server_out = '' if args.verbose else '> /dev/null'
        subprocess.call('cd ../.. && ant our-server ' + server_out + ' &', shell=True)
        time.sleep(args.build_time)

        # run tests
        unittest.main()

    finally:
        # kill the server now
        subprocess.call(SERVER_KILL_COMMAND, shell=True)
