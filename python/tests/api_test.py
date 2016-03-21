
import unittest
import requests


class LoginTests(unittest.TestCase):
    pass

if __name__ == '__main__':
    print('ayyeee')
    r = requests.get('https://www.google.com')
    print(r.status_code)
    unittest.main()
