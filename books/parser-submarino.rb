require 'open-uri'
require 'nokogiri'

base = 'http://www.submarino.com.br/linha/309886/livros/mais-vendidos?ofertas.limit=20'
page = Nokogiri::HTML(open(base))

products = page.css(".top-area-product")
names = Array.new

products.each  do |product|
	name =  product.css("span")[0].to_s
	first = name.index('>') + 2
	last = name.index('<',2) - 1
	name = name[first..last]
	names.push(name)
end

products = page.css('.product-price-integer')
prices = Array.new

products.each  do |product|
	text = product.to_s
	first = text.index('>') + 1
	last = text.index('<', 2) - 1
	price = text[first..last]
	prices.push(price)
end

file = File.open "books.data", "w"
random = Random.new

names.size.times do |i|
	filename = names[i].gsub(" ", "_")
	file.write(filename+"\n")
	book = File.open filename, "w"
	book.write(random.rand(10).to_s+"\\"+prices[i]+"\n")
	book.close
end
