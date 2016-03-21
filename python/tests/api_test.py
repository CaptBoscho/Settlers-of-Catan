
import unittest
import requests
import subprocess
import time
import json
import argparse


'''
    Integration tests with Python and the Requests library

    http://docs.python-requests.org/en/master/
    https://docs.python.org/2/library/unittest.html
    https://docs.python.org/2/library/subprocess.html
    https://docs.python.org/2/library/json.html
'''


SERVER_KILL_COMMAND = "lsof -i tcp:8081 | awk 'NR!=1 {print $2}' | xargs kill"
BASE_URL = 'http://localhost:8081/'
created_user_count = 0


class JsonValidationTests(unittest.TestCase):
    ''' Ensures that bad/malformed JSON returns a 400 error. Only applicable
        to POST endpoints.
    '''

    def setUp(self):
        bad_payload = {
            'macklemore': 'ryan lewis'
        }
        self.bad_json = json.dumps(bad_payload)
        self.not_even_json = 'hi hello hey'

    # -------- user
    def test_bad_register_json(self):
        r = requests.post('%suser/register' % BASE_URL, data=self.bad_json)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%suser/register' % BASE_URL, data=self.not_even_json)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_login_json(self):
        r = requests.post('%suser/login' % BASE_URL, data=self.bad_json)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%suser/login' % BASE_URL, data=self.not_even_json)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- games
    def test_bad_create_game_json(self):
        r = requests.post('%sgames/create' % BASE_URL, data=self.bad_json)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%sgames/create' % BASE_URL, data=self.not_even_json)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_join_game_json(self):
        r = requests.post('%sgames/join' % BASE_URL, data=self.bad_json)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%sgames/join' % BASE_URL, data=self.not_even_json)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)


class EndpointFilterTests(unittest.TestCase):
    ''' Most endpoints have some kind of filter to either ensure the user is
        authenticated or currenlty in a game. This test suite will ensure that
        those filters are doing their job.
    '''
    # TODO
    pass


class HttpMethodTests(unittest.TestCase):
    ''' Validates that the endpoints only support the methods they should '''

    # -------- user
    def test_GET_login(self):
        r = requests.get('%suser/login' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_register(self):
        r = requests.get('%suser/register' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- games
    def test_POST_list_games(self):
        r = requests.post('%sgames/list' % BASE_URL, data='')
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_create_game(self):
        r = requests.get('%sgames/create' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_join_game(self):
        r = requests.get('%sgames/join' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- game
    def test_POST_game_model(self):
        r = requests.post('%sgame/model' % BASE_URL, data='')
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_add_ai(self):
        r = requests.get('%sgame/addAI' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_POST_list_ai(self):
        r = requests.post('%sgame/listAI' % BASE_URL, data='')
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- moves
    def test_GET_send_chat(self):
        r = requests.get('%smoves/sendChat' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_roll_number(self):
        r = requests.get('%smoves/rollNumber' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_rob_player(self):
        r = requests.get('%smoves/robPlayer' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_finish_turn(self):
        r = requests.get('%smoves/finishTurn' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_buy_dev_card(self):
        r = requests.get('%smoves/buyDevCard' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_year_of_plenty(self):
        r = requests.get('%smoves/Year_of_Plenty' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_road_building(self):
        r = requests.get('%smoves/Road_Building' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_soldier(self):
        r = requests.get('%smoves/Soldier' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_monopoly(self):
        r = requests.get('%smoves/Monopoly' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_monument(self):
        r = requests.get('%smoves/Monument' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_build_road(self):
        r = requests.get('%smoves/buildRoad' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_build_settlement(self):
        r = requests.get('%smoves/buildSettlement' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_build_city(self):
        r = requests.get('%smoves/buildCity' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_offer_trade(self):
        r = requests.get('%smoves/offerTrade' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_accept_trade(self):
        r = requests.get('%smoves/acceptTrade' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_maritime_trade(self):
        r = requests.get('%smoves/maritimeTrade' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_discard_cards(self):
        r = requests.get('%smoves/discardCards' % BASE_URL)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)


class UserTests(unittest.TestCase):

    def setUp(self):
        requests.get('%stest/reset' % BASE_URL)

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
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(payload))
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual('Success', r.text)
        self.assertTrue('catan.user' in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)
        self.assertEqual(expected_cookie, r.cookies['catan.user'])

        # should now be able to login with these credentials
        r = requests.post('%suser/login' % BASE_URL, data=json.dumps(payload))
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual('Success', r.text)
        self.assertTrue('catan.user' in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)
        self.assertEqual(expected_cookie, r.cookies['catan.user'])


class GamesTests(unittest.TestCase):

    def setUp(self):
        requests.get('%stest/reset' % BASE_URL)

        # populate the server with a user
        self.test_username = 'games_user'
        self.test_password = 'games_password'
        payload = {
            'username': self.test_username,
            'password': self.test_password
        }
        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(payload))
        self.cookies = r.cookies

    def test_creating_a_game(self):
        pass


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
