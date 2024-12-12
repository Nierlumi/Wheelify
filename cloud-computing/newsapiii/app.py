from flask import Flask, request, jsonify
from flask_cors import CORS
import requests

app = Flask(__name__)
CORS(app)

# API Key dan URL untuk layanan berita
NEWS_API_KEY = "7794ee85713542fc8e2c2d3b0ce89449"
NEWS_API_URL = "https://newsapi.org/v2/top-headlines"

@app.route('/', methods=['GET'])
def home():
    """
    Endpoint untuk root. Menyediakan informasi penggunaan API.
    """
    return jsonify({
        'message': 'Welcome to the News API! Use /news or /news/<id> endpoint.',
        'endpoints': {
            '/news': 'Get the latest news.',
            '/news/<id>': 'Get news by unique ID.'
        }
    })

@app.route('/news', methods=['GET'])
def get_news():
    """
    Endpoint untuk mendapatkan daftar berita dengan ID unik ribuan.
    """
    try:
        # Ambil parameter dari URL
        country = request.args.get('country', 'us')  # Default: US
        category = request.args.get('category', 'general')  # Default: General

        # Fungsi untuk mengambil artikel dari API
        def fetch_articles(country, category):
            response = requests.get(NEWS_API_URL, params={
                'apiKey': NEWS_API_KEY,
                'country': country,
                'category': category,
            })
            if response.status_code == 200:
                return response.json().get('articles', [])
            else:
                raise Exception(f"Error fetching articles: {response.status_code}, {response.text}")

        # Ambil artikel dari News API
        articles = fetch_articles(country, category)

        # Cek jika tidak ada artikel
        if not articles:
            return jsonify({
                'status': 'error',
                'message': 'No articles found for the given parameters.'
            }), 404

        # Format ulang data artikel dengan ID unik ribuan
        formatted_articles = []
        base_id = 1000  # ID awal dimulai dari 1000
        for index, article in enumerate(articles):
            title = article.get('title', 'No title available')
            description = article.get('description', 'No description available')
            image = article.get('urlToImage', 'No image available')
            url = article.get('url', 'No URL available')

            formatted_articles.append({
                'id': base_id + index,  # Tambahkan ID unik ribuan
                'title': title,
                'description': description,
                'image': image,
                'url': url
            })

        return jsonify({'status': 'success', 'articles': formatted_articles})
    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 500

@app.route('/news/<int:article_id>', methods=['GET'])
def get_news_by_id(article_id):
    """
    Endpoint untuk mendapatkan berita berdasarkan ID unik.
    """
    try:
        # Ambil parameter country dan category
        country = request.args.get('country', 'us')
        category = request.args.get('category', 'general')

        # Fungsi untuk mengambil artikel dari API
        def fetch_articles(country, category):
            response = requests.get(NEWS_API_URL, params={
                'apiKey': NEWS_API_KEY,
                'country': country,
                'category': category,
            })
            if response.status_code == 200:
                return response.json().get('articles', [])
            else:
                raise Exception(f"Error fetching articles: {response.status_code}, {response.text}")

        # Ambil artikel
        articles = fetch_articles(country, category)

        # Format ulang artikel dengan ID unik ribuan
        base_id = 1000
        formatted_articles = []
        for index, article in enumerate(articles):
            formatted_articles.append({
                'id': base_id + index,
                'title': article.get('title', 'No title available'),
                'description': article.get('description', 'No description available'),
                'image': article.get('urlToImage', 'No image available'),
                'url': article.get('url', 'No URL available')
            })

        # Cari artikel berdasarkan ID
        selected_article = next((article for article in formatted_articles if article['id'] == article_id), None)

        # Jika artikel tidak ditemukan
        if not selected_article:
            return jsonify({
                'status': 'error',
                'message': f'Article with ID {article_id} not found.'
            }), 404

        return jsonify({'status': 'success', 'article': selected_article})
    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
