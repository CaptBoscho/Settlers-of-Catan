# -*- coding: utf-8 -*-
__author__ = 'Derek Argueta'

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
        requests.get('%stest/reset' % BASE_URL)
        bad_payload = {
            'macklemore': 'ryan lewis'
        }
        self.bad_json = json.dumps(bad_payload)
        self.not_even_json = 'hi hello hey'

        # authenticate myself with a user to get past the authentication filter
        self.test_username = 'json_validation_user'
        self.test_password = 'json_validation_password'
        payload = {
            'username': self.test_username,
            'password': self.test_password
        }
        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(payload))
        self.cookies = {}
        if 'catan.user' in r.cookies:
            self.cookies['catan.user'] = r.cookies['catan.user']
        if 'catan.game' in r.cookies:
            self.cookies['catan.game'] = r.cookies['catan.game']

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
        r = requests.post('%suser/login' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%suser/login' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- games
    def test_bad_create_game_json(self):
        r = requests.post('%sgames/create' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%sgames/create' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_join_game_json(self):
        r = requests.post('%sgames/join' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%sgames/join' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- game
    def test_bad_add_ai_json(self):
        r = requests.post('%sgame/addAI' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%sgame/addAI' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- moves
    def test_bad_send_chat_json(self):
        r = requests.post('%smoves/sendChat' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/sendChat' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_roll_number_json(self):
        r = requests.post('%smoves/rollNumber' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/rollNumber' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    @unittest.skip('not yet implemented')
    def test_bad_rob_player_json(self):
        r = requests.post('%smoves/robPlayer' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/robPlayer' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_finish_turn_json(self):
        r = requests.post('%smoves/finishTurn' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/finishTurn' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    @unittest.skip('not yet implemented')
    def test_bad_buy_dev_card_json(self):
        r = requests.post('%smoves/buyDevCard' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/buyDevCard' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_year_of_plenty_json(self):
        r = requests.post('%smoves/Year_of_Plenty' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/Year_of_Plenty' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    @unittest.skip('not yet implemented')
    def test_bad_road_building_json(self):
        r = requests.post('%smoves/Road_Building' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/Road_Building' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    @unittest.skip('not yet implemented')
    def test_bad_soldier_json(self):
        r = requests.post('%smoves/Soldier' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/Soldier' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_monopoly_json(self):
        r = requests.post('%smoves/Monopoly' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/Monopoly' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_monument_json(self):
        r = requests.post('%smoves/Monument' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/Monument' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    @unittest.skip('not yet implemented')
    def test_bad_build_road_json(self):
        r = requests.post('%smoves/buildRoad' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/buildRoad' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    @unittest.skip('not yet implemented')
    def test_bad_build_settlement_json(self):
        r = requests.post('%smoves/buildSettlement' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/buildSettlement' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    @unittest.skip('not yet implemented')
    def test_bad_build_city_json(self):
        r = requests.post('%smoves/buildCity' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/buildCity' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    @unittest.skip('not yet implemented')
    def test_bad_offer_trade_json(self):
        r = requests.post('%smoves/offerTrade' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/offerTrade' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_accept_trade_json(self):
        r = requests.post('%smoves/acceptTrade' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/acceptTrade' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_maritime_trade_json(self):
        r = requests.post('%smoves/maritimeTrade' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/maritimeTrade' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_bad_discard_cards_json(self):
        r = requests.post('%smoves/discardCards' % BASE_URL, data=self.bad_json, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Invalid request.', r.text)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

        r = requests.post('%smoves/discardCards' % BASE_URL, data=self.not_even_json, cookies=self.cookies)
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

    def setUp(self):
        requests.get('%stest/reset' % BASE_URL)

        # authenticate myself with a user to get past the authentication filter
        self.test_username = 'json_validation_user'
        self.test_password = 'json_validation_password'
        payload = {
            'username': self.test_username,
            'password': self.test_password
        }
        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(payload))
        self.cookies = {}
        if 'catan.user' in r.cookies:
            self.cookies['catan.user'] = r.cookies['catan.user']
        if 'catan.game' in r.cookies:
            self.cookies['catan.game'] = r.cookies['catan.game']

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
        r = requests.post('%sgames/list' % BASE_URL, data='', cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_create_game(self):
        r = requests.get('%sgames/create' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_join_game(self):
        r = requests.get('%sgames/join' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- game
    def test_POST_game_model(self):
        r = requests.post('%sgame/model' % BASE_URL, data='', cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_add_ai(self):
        r = requests.get('%sgame/addAI' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_POST_list_ai(self):
        r = requests.post('%sgame/listAI' % BASE_URL, data='', cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    # -------- moves
    def test_GET_send_chat(self):
        r = requests.get('%smoves/sendChat' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_roll_number(self):
        r = requests.get('%smoves/rollNumber' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_rob_player(self):
        r = requests.get('%smoves/robPlayer' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_finish_turn(self):
        r = requests.get('%smoves/finishTurn' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_buy_dev_card(self):
        r = requests.get('%smoves/buyDevCard' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_year_of_plenty(self):
        r = requests.get('%smoves/Year_of_Plenty' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_road_building(self):
        r = requests.get('%smoves/Road_Building' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_soldier(self):
        r = requests.get('%smoves/Soldier' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_monopoly(self):
        r = requests.get('%smoves/Monopoly' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_monument(self):
        r = requests.get('%smoves/Monument' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_build_road(self):
        r = requests.get('%smoves/buildRoad' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_build_settlement(self):
        r = requests.get('%smoves/buildSettlement' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_build_city(self):
        r = requests.get('%smoves/buildCity' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_offer_trade(self):
        r = requests.get('%smoves/offerTrade' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_accept_trade(self):
        r = requests.get('%smoves/acceptTrade' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_maritime_trade(self):
        r = requests.get('%smoves/maritimeTrade' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.not_found, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' not in r.cookies)

    def test_GET_discard_cards(self):
        r = requests.get('%smoves/discardCards' % BASE_URL, cookies=self.cookies)
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
        self.cookies = {}
        if 'catan.user' in r.cookies:
            self.cookies['catan.user'] = r.cookies['catan.user']
        if 'catan.game' in r.cookies:
            self.cookies['catan.game'] = r.cookies['catan.game']

    def test_creating_a_game(self):
        r = requests.get('%sgames/list' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual(0, len(r.json()))

        payload = {
            'name': 'test 1',
            'randomTiles': False,
            'randomNumbers': False,
            'randomPorts': False
        }
        json_payload = json.dumps(payload)
        r = requests.post('%sgames/create' % BASE_URL, data=json_payload, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' in r.cookies)
        self.cookies['catan.game'] = r.cookies['catan.game']

        r = requests.get('%sgames/list' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual(1, len(r.json()))

        # create another game with a different name
        payload['name'] = 'test 2'
        json_payload = json.dumps(payload)
        r = requests.post('%sgames/create' % BASE_URL, data=json_payload, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' in r.cookies)
        self.cookies['catan.game'] = r.cookies['catan.game']

        r = requests.get('%sgames/list' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual(2, len(r.json()))

        # create another game with a different name
        payload['name'] = 'test 3'
        json_payload = json.dumps(payload)
        r = requests.post('%sgames/create' % BASE_URL, data=json_payload, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' in r.cookies)
        self.cookies['catan.game'] = r.cookies['catan.game']

        r = requests.get('%sgames/list' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual(3, len(r.json()))

        # create another game with a different name
        payload['name'] = 'test 4'
        json_payload = json.dumps(payload)
        r = requests.post('%sgames/create' % BASE_URL, data=json_payload, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertTrue('catan.user' not in r.cookies)
        self.assertTrue('catan.game' in r.cookies)
        self.cookies['catan.game'] = r.cookies['catan.game']

        r = requests.get('%sgames/list' % BASE_URL, cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertEqual(4, len(r.json()))

        # verify we can't make a game with a duplicate name
        json_payload = json.dumps(payload)
        r = requests.post('%sgames/create' % BASE_URL, data=json_payload, cookies=self.cookies)
        self.assertEqual(requests.codes.bad_request, r.status_code)
        self.assertEqual('Game name taken.', r.text)

    def test_joining_a_game(self):
        # create a random game to start with
        payload = {
            'name': 'test 1',
            'randomTiles': False,
            'randomNumbers': False,
            'randomPorts': False
        }
        json_payload = json.dumps(payload)
        r = requests.post('%sgames/create' % BASE_URL, data=json_payload, cookies=self.cookies)

        # create 3 more players and have them join the game
        payload = {
            'username': 'user1',
            'password': 'user1'
        }
        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(payload))
        self.cookies['catan.user'] = r.cookies['catan.user']
        payload = {
            'id': 0,
            'color': 'red'
        }
        r = requests.post('%sgames/join' % BASE_URL, data=json.dumps(payload), cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertTrue('catan.game' in r.cookies)
        self.assertEqual(0, int(r.cookies['catan.game']))

        payload = {
            'username': 'user2',
            'password': 'user2'
        }
        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(payload))
        self.cookies['catan.user'] = r.cookies['catan.user']
        payload = {
            'id': 0,
            'color': 'blue'
        }
        r = requests.post('%sgames/join' % BASE_URL, data=json.dumps(payload), cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertTrue('catan.game' in r.cookies)
        self.assertEqual(0, int(r.cookies['catan.game']))

        payload = {
            'username': 'user3',
            'password': 'user3'
        }
        r = requests.post('%suser/register' % BASE_URL, data=json.dumps(payload))
        self.cookies['catan.user'] = r.cookies['catan.user']
        payload = {
            'id': 0,
            'color': 'yellow'
        }
        r = requests.post('%sgames/join' % BASE_URL, data=json.dumps(payload), cookies=self.cookies)
        self.assertEqual(requests.codes.ok, r.status_code)
        self.assertTrue('catan.game' in r.cookies)
        self.assertEqual(0, int(r.cookies['catan.game']))

        # TODO - add test to verify you can't have more than 4 users in a game
        # TODO - pull game model to verify user info that's added to the game


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
